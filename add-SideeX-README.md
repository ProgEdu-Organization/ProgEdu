# ProgEdu4Web with K8s and SideeX
在本架構中，我們使用Selenium Grid將Jenkins中動態測試及截圖的工作分散處理並且將K8s當作分散式系統的管理工具。
![](https://i.imgur.com/tJcRhNk.png)
要完成本架構，首先必須先建立K8s的叢集然後在K8s上部屬Selenium Grid以此來將我們的工作做分散式處理。

## 建立K8s叢集
這個部分我們使用kubeadm來幫助我們快速建立叢集，可以同時參考[官方文件](https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/)。
開始建立之前請先確定Docker的版本為docker-ce而不是docker. io，安裝docker-ce請參考[這裡](https://docs.docker.com/engine/install/ubuntu/)。

### 1. 安裝必要套件
在Master Node和Worker Node上安裝**kubeadm、kubelet、kubectl**：
```
sudo apt-get update && sudo apt-get install -y apt-transport-https curl
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
cat <<EOF | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF
sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl
```
### 2. 在叢集中部屬內部網路
這個步驟將對Master Node做初始化同時部屬內部網路，K8s本身提供了多種應用來實現內部網路的部屬，我們這裡示範的是**Calico**，網路部屬可同時參考[官方文件](https://docs.projectcalico.org/getting-started/kubernetes/quickstart)。

初始化Master Node：
```
sudo kubeadm init --pod-network-cidr=192.168.0.0/16
```
完成後會看到以下輸出：
```
[init] Using Kubernetes version: vX.Y.Z
[preflight] Running pre-flight checks
[preflight] Pulling images required for setting up a Kubernetes cluster
.
.
.
You can now join any number of machines by running the following on each node
as root:

  kubeadm join <control-plane-host>:<control-plane-port> --token <token> --discovery-token-ca-cert-hash sha256:<hash>
```
其中最後一行的指令為將其他機器加入此叢集所使用，請在完成下面網路部屬之後再將使用此指令將其他機器加入叢集中。

設定kubectl，使其可以被一般使用者操作：
```
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

安裝Calico：
```
kubectl create -f https://docs.projectcalico.org/manifests/tigera-operator.yaml
```

移除taints：
```
kubectl taint nodes --all node-role.kubernetes.io/master-
```

檢查當前Node的狀態：
```
kubectl get nodes -o wide
```
會看到以下輸出：
```
NAME              STATUS   ROLES  ...
<your-hostname>   Ready    master ...
```
**STATUS**欄位若顯示**Ready**即代表成功

### 3. 將其他機器加入叢集、新增標籤
在第二步中當完成初始化時有一串加入叢集的指令：
```
kubeadm join <control-plane-host>:<control-plane-port> --token <token> --discovery-token-ca-cert-hash sha256:<hash>
```
在欲加入叢集的機器上輸入後即可完成，同樣地可以使用```kubectl get nodes -o wide```查看節點狀態。

接下來為了確保後續Selenium Grid的應用有部屬至正確的節點，我們需要分別給予Master Node及Worker Node標籤。
執行：
```
kubectl label node/<your_master_machine_hostname> role=master
kubectl label node/<your_worker_machine_hostname> role=worker
```
可將作為Master Node的機器標為"master"，將作為Worker Node的機器標為"worker"。

## 部屬Selenium Grid至K8s
這個部分我們要將Selenium Grid部屬至已經架好的K8s叢集上，相關流程可同時參考[這裡](https://github.com/kubernetes/examples/tree/master/staging/selenium)。
首先，建立Selenium Hub部屬檔*selenium-hub-deployment.yaml*：
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: selenium-hub
  labels:
    app: selenium-hub
spec:
  replicas: 1
  selector:
    matchLabels:
      app: selenium-hub
  template:
    metadata:
      labels:
        app: selenium-hub
    spec:
      containers:
      - name: selenium-hub
        image: selenium/hub:3.141
        ports:
          - containerPort: 4444
        resources:
          limits:
            memory: "1000Mi"
            cpu: ".5"
        livenessProbe:
          httpGet:
            path: /wd/hub/status
            port: 4444
          initialDelaySeconds: 30
          timeoutSeconds: 5
        readinessProbe:
          httpGet:
            path: /wd/hub/status
            port: 4444
          initialDelaySeconds: 30
          timeoutSeconds: 5
      nodeSelector:
        role: master
```
建立網路服務*selenium-hub-svc.yaml*：
```
apiVersion: v1
kind: Service
metadata:
  name: selenium-hub
  labels:
    app: selenium-hub
spec:
  ports:
  - port: 4444
    targetPort: 4444
    protocol: TCP
    nodePort: 30080
    name: port0
  selector:
    app: selenium-hub
  type: NodePort
  sessionAffinity: None
```
其中的**nodePort**為Selenium Grid的進入點，可修改。

建立Selenium Node部屬檔*selenium-node-chrome-deployment.yaml*：
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: selenium-node-chrome
  labels:
    app: selenium-node-chrome
spec:
  replicas: 10
  selector:
    matchLabels:
      app: selenium-node-chrome
  template:
    metadata:
      labels:
        app: selenium-node-chrome
    spec:
      volumes:
      - name: dshm
        emptyDir:
          medium: Memory
      containers:
      - name: selenium-node-chrome
        image: selenium/node-chrome:3.141.59
        ports:
          - containerPort: 5555
        volumeMounts:
          - mountPath: /dev/shm
            name: dshm
        env:
          - name: HUB_HOST
            value: "selenium-hub"
          - name: HUB_PORT
            value: "4444"
        resources:
          limits:
            memory: "1000Mi"
            cpu: ".5"
      nodeSelector:
        role: worker
```
其中**replicas**可以設定Selenium Node的初始數量。

部屬Selenium Hub：
```
kubectl create -f selenium-hub-deployment.yaml
```
部屬service：
```
kubectl create -f selenium/selenium-hub-svc.yaml
```
部屬Selenium Node：
```
kubectl create -f selenium-node-chrome-deployment.yaml
```
完成後即可透過```http://<your_master_machine_host>:30080```使用Selenium Grid

# ProgEdu架設
請使用分支 https://github.com/fcumselab/ProgEdu/tree/web_sideex

**WEB_SELENIUM_URL**請填入上一章節中Selenium Grid的進入點
![](https://i.imgur.com/JvLqj4f.png)

在完成Jenkins的設定後，請先至 https://github.com/fcumselab/SideeXThrowError 下載SideeX外掛，然後使用Maven打包再上傳到Jenkins
