![](/readme-images/logo.png)
# 簡介
是一個自動檢查程式碼與編譯程式碼後，`批改`, `統計`, `測試` 的系統。

主要為調用Jenkins與GitLab的API服務, 且將整個系統用到的服務 **容器化** 運行。

# 建置流程教學
## 首先確認機器上是否有 `docker-compose` 與 `docker` 軟體
```
docker-compose -version // 1.26.0
docker -v // 19.03.11
```
## 初步建置步驟
1. 在linux系統中clone [ProgEdu專案](https://github.com/fcumselab/ProgEdu), 的develop分支

`git clone --depth 1  --branch developer --single-branch https://github.com/fcumselab/ProgEdu.git`

1. 將 `env-example`檔案改名成 `.env` (注意是 "點 env" ), 並編輯它, 它將用來作為建置的設定。

   2.1. 修正 `GITLAB_HOST=http://example.com:22080` 成 
   `GITLAB_HOST=http://你電腦的IP地址:22080`。(22080 需要與 `.env` 裡的 `GITLAB_HTTP_PORT`設定一致)。
   
   2.2.如上一步, 去修正其它像右邊這種特徵的網址 `http://example.com` 成實際上ProgEdu要架設到的地方, 記得每個服務的 port 都要跟網址後面的 port 一致, 像是 `JENKINS_PORT=8080` 與 `WEB_JENKINS_URL=http://example.com:8080`,可以注意到後面的8080是一樣的, 所以如果因為port號被占用, 而要更換成38080, 則兩個設定為`JENKINS_PORT=38080` 與 `WEB_JENKINS_URL=http://example.com:38080`。

   2.3. 更改 `COMPOSE_PROJECT_NAME=ProgEdu`, 後面的`ProgEdu`可以修改成`MyProgEdu`, 這是表示這群容器的唯一識別ID, 所以如果同一電腦部屬了兩組ProgEdu, 則這變數必須不一樣, 才不會有衝突。

2. 安全性設定的重要細節 : 
   
   3.1. 如果要更改資料庫密碼, 則 `DB_PASSWORD` 和 `DB_ROOT_PASSWORD` ,必須設定一樣。
   
   3.2. `GITLAB_ROOT_PASSWORD` 是`server`的root的密碼, 也是`GitLab`的root的密碼。
   
   3.3. 補充上述, `server`是指ProgEdu的主要網站服務, 之後網站管理員的進入帳號是root, 密碼是上述所設定的。
  
3. 在專案的根目錄執行 `sudo docker-compose up -d` 即可完成初步建置。

4. 初步建置步驟結束,接下來需要各別設定 GitLab 和 Jenkins 的一些權限，這樣ProgEdu才能跟這兩個服務連動。

## **設定 GitLab 流程**
### 1. 登入
用瀏覽器依照 `.env`裡的 `GITLAB_HOST` 的網址進入GitLab，
使用者名稱為root，密碼為 `GITLAB_ROOT_PASSWORD`對應的值
### 2. 複製GitLab Token 
右上方頭像進入 > `Settings` ,在左邊導覽列找到 `Account Tokens`後， 
可自行設定Name, 日期可以不用設定,
接著全部權限打勾後, 按下`Create personal access token`, 將產生的 **GitLab Token** 
輸入到`.env`的 `WEB_GITLAB_ADMIN_PERSONAL_TOKEN`的值,如下範例 
`WEB_GITLAB_ADMIN_PERSONAL_TOKEN=wek213wlkawjrlamsdkfa`

## **設定 Jenkins 流程**  

1. 依`.env`的`WEB_JENKINS_URL`所設定的網址進入Jenkins 
2. 解鎖Jenkins
![](readme-images/unlock-jenkins.png)
(備註 解鎖Jenkins需要進入容器查看 initialAdminPassword 檔案) 以下是進入容器方法
```
docker exec -it (your_Jenkins_container_name) bash 
cat /var/jenkins_home/secrets/initialAdminPassword
``` 
(備註: your_Jenkins_container_name 可以從docker-compose時得到, 如下圖範例
![](/readme-images/how-into-jenkins-container.png)  

3. 安裝plug-in 選擇"Install suggested plugins"
![](/readme-images/jenkins-install-plugins.png)  

4. 填上使用者資訊 
![](/readme-images/jenkins-create-admin-user.png)  

這裡的名稱 跟密碼 設定必須跟 `.env` 的設定 一致
所以你要先查看你的`.env`。 例如下面例子的`.env`
```
WEB_JENKINS_ADMIN_USERNAME=admin
WEB_JENKINS_ADMIN_PASSWORD=admin
```
則在此例下, 就必須設定Jenkins的帳號: `admin`, 密碼: `admin`
, 按下 `Save and Finish`
接著持續按下一步， 與登入, 就會完成初步的Jenkins架設流程,
還剩幾步驟是為了拿權限。

5. 拿取**Jenkins Token** 
因為ProgEdu要跟Jenkins做溝通 所以需要 **Jenkins Token**

右上角點選 `使用者名稱 > 設定 > API Token > Add new Token > Generate > 複製Token > 儲存`
![](/readme-images/jenkins-token.jpg)  

拿到**Jenkins Token**後要去設定 `.env` 檔
`WEB_JENKINS_API_TOKEN={Jenkins Token}`(提醒: 大括號要拿掉)

6. 開啟讀取權限
因為ProgEdu需要用它提供的API來讀取`Jenkins`建置完成的檔案。

`管理jenkins > 設定全域安全性中 > 打勾 Allow anonymous read access > 儲存`
![](/readme-images/jenkins-access-control.png)

7. 設定**GitLab API Token**

因為jenkins也必須要跟GitLab請求程式碼，所以必須給他gitlab的token。

7_1. `管理Jenkins > 設定系統` 找到 GitLab後，
先把(Enable authentication for ... connection) 的勾取消
 
7_2. 設定 Connection name = gitlab

7_3. 設定 GitLab host URL 為`.env`文件設定的 gitlab 網址
![](/readme-images/jenkins-gitlab-api.png)  

7_4. 設定 Credentials:  如下步驟
1. 按 Add > Jenkins
2. Kind = GitLab API token
3. Scope = Glibal
4. API token填上 你之前存在`.env`的**GitLab Token**
5. ID = gitlab_api
 ![](/readme-images/jenkins-credentials.jpg)  
6. 新增後把Credentials 換成 `GitLab API token` 
7. 點擊Test Connection測試是否成功
8. **儲存**
9. 可以在jenkins -> Credentials 發現你剛剛設定的憑證

10. 設定GitLab憑證
    
`Credentials > System > Global credentials (unrestricted) > Add Credentials`

- Username：{GitLab root username} (在你剛剛設定的.env找)
- Password：{GitLab root password} (在你剛剛設定的.env找)  
- ID: 必須是 **gitlab_root**  

![](/readme-images/jenkins-credentials-gitlab.jpg)

**全部設定結束後，儲存.env檔，然後要重新下 `docker-compose up -d` 指令，目的是用新的設定重新建置一次**
## 測試系統建置成功
1. 進入 `.env` 所設定的 `WEB_EXTERNAL_URL` 的網址
2. 輸入帳號: root, 密碼: `.env` 的 `GITLAB_ROOT_PASSWORD` 的值
3. 可以測試創使用者後, 會發現GitLab也會新增一個使用者
4. 詳細功能目前還沒有文件來說明， 待之後補上

## 推薦開發ProgEdu的重要工具
* MobeXterm => 用ssh來連線linux
* GitKraken => 版本控制
* Postman => 測試web API功能
* MySQL workbench => 用來查看資料庫

## 學習過程推薦
1. Docker > Docker Compose > MySQL > RESTful API >  GitLab > Jenkins > JAVA > Maven > NodeJS > NPM 
