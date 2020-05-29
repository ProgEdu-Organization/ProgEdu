![](https://github.com/fcumselab/ProgEdu/blob/developer/server/front/src/assets/img/logo.png)
# ProgEdu介紹

# ProgEdu建置流程(2020/05/28更新)

### 首先確認server是否有docker-compose與docker工具
```
docker-compose  
docker -v
```
#### 建置步驟
1. 在linux 中 clone [ProgEdu專案](https://github.com/fcumselab/ProgEdu)
2. 將env-example文件修改成`.env` (注意是 **".env"** ) 
*  修正 DB_USER=root
*  設定密碼 DB_PASSWORD 要和 DB_PASSWORD 一樣 
*  修正 `GITLAB_HOST=http://gitlab.example.com:22080` 成 
   `GITLAB_HOST=http://host:port`
*  如上一步 去修正其他所有 `http://example.com` 的路徑
3. 執行`sudo docker-compose up -d` 

### **Gitlab**
##### 1. 登入
依docker-compose.yml文件設定網址進入Gitlab
使用者名稱為root，密碼為.env設定之密碼 
##### 2. 複製Gitlab Token
右上方頭像進入 > `Settings` ,再右邊導覽列找到 `Account Tokens` 
全部權限打勾然後產生**Gitlab Token** 
這是用來讓ProgEdu可以對Gitlab 控制的方法
而ProgEdu的設定都源自於.env
所以想當然爾要去.env設定 
`WEB_GITLAB_ADMIN_PERSONAL_TOKEN = {Gitlab API Token}`
將 **{Gitlab API Token}** 替換成 **Gitlab Token** (提醒 大括號要被刪掉)

### **Jenkins**  

1. 依docker-compose.yml文件設定網址進入Jenkins (第一次進入他會要你Unlock Jenkins)
![](https://github.com/fcumselab/ProgEdu/blob/developer/readme-images/unlock-jenkins.png)
2. 解鎖Jenkins 進入容器 > 查看 initialAdminPassword 檔案 > 貼到上面輸入欄
```
docker exec -it (YOUR_JENKINS_hash) bash 
cat /var/jenkins_home/secrets/initialAdminPassword
``` 
3. 安裝plug-in 選擇"Install suggested plugins"
![](https://github.com/fcumselab/ProgEdu/blob/developer/readme-images/jenkins-install-plugins.png)  

4. 填上使用者資訊 
![](https://github.com/fcumselab/ProgEdu/blob/developer/readme-images/jenkins-create-admin-user.png)  

這裡的名稱 跟密碼 設定必須跟 `.env` 的設定 一致
所以你要先查看你的`.evn` 把你設定的帳密填入以下兩行
```
WEB_JENKINS_ADMIN_USERNAME=yaya
WEB_JENKINS_ADMIN_PASSWORD=password
```
> 完成Jenkins初始化程序

5. 拿取**Jenkins Token** 
因為ProgEdu要跟Jenkins做溝通 所以需要 **Jenkins Token**
右上角點選 使用者名稱 > 設定 > API Token > Add new Token > Generate > 複製Token > **存**
![](https://github.com/fcumselab/ProgEdu/blob/developer/readme-images/jenkins-token.jpg)  

拿到**Jenkins Token**後要去設定 `.env` 檔
`WEB_JENKINS_API_TOKEN={Jenkins Token}`

6. 開啟讀取權限
因為ProgEdu需要讀Jenkins建置完成的檔案
在Jenkins  管理jenkins > 設定全域安全性中 > 打勾 Allow anonymous read access > **儲存**
![](https://github.com/fcumselab/ProgEdu/blob/developer/readme-images/jenkins-access-control.png)

5. 設定**Gitlab API Token**
因為jenkins必須要跟Gitlab要程式碼，所以必須給他gitlab的token
    1. 管理Jenkins > 設定系統 找到 Gitlab 
    先把(Enable authentication for ... connection) 的勾 取消 
    設定 Connection name = gitlab
    設定 Gitlab host URL 填上.env文件設定的 gitlab 網址
    ![](https://github.com/fcumselab/ProgEdu/blob/developer/readme-images/jenkins-gitlab-api.png)  
    1. 設定 Credentials(證書):  如下步驟
        1. 按 Add > Jenkins
        2. Kind = Gitlab API token
        3. Scope = Glibal
        4. API token填上 你之前存在`.env`的**Gitlab Token**
        5. ID = gitlab_api
        ![](https://github.com/fcumselab/ProgEdu/blob/developer/readme-images/jenkins-credentials.jpg)  
    2. 新增後把Credentials 換成 `GitLab API token` 
    3. 點擊Test Connection測試是否成功
    4. **儲存**
    5. 可以在jenkins -> Credentials 發現你剛剛設定的憑證

6. 設定Gitlab憑證
    Credentials > System > Global credentials (unrestricted) > Add Credentials
    - Username：Gitlab root username (去你剛剛設定的.env找)
    - Password：Gitlab root password (去你剛剛設定的.env找)  
    - ID: 必須是 **gitlab_root**  
    
    ![](https://github.com/fcumselab/ProgEdu/blob/developer/readme-images/jenkins-credentials-gitlab.jpg)

**全部設定結束後 要重新下 docker-compose up -d**


## 推薦開發ProgEdu的重要工具
* MobeXterm => 用ssh遠端連線linux
* sourcetree => 版本控制
* postman => 測試web API功能
* MySQL workbench => 用來查看資料庫
