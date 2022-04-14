1-Install OwaspZAP

With this link, we can download OWASP ZAP:	https://www.zaproxy.org/download/

Choose the appropriate installer for your system and click Download button.I will explain how you can   download and install the ZAP of the Linux version.

1.1- Click Download button for Linux Installer

![image](https://user-images.githubusercontent.com/80597081/163392031-75a4d8cc-8e0b-47a9-9c8c-a0eb60c09aa9.png)

1.2- Choose Save File option then click OK

![image](https://user-images.githubusercontent.com/80597081/163392095-30f4cdd0-ece6-45b9-a78c-b275a41f63e1.png)

1.3- Open Terminal and go to the directory that you download ZAP Linux Installer.

1.4- Write this command to install ZAP which is extended such as .sh. The installer’s name may change because of the installer’s version, so type your installer’s name which is .sh extended file.

   → 		 		chmod o+x ZAP_2_11_1_unix.sh	

   →		        ./ZAP_2_11_1_unix.sh

If you face with an error, write this command with sudo at the beginning of the command. 	Otherwise do not execute this code

   →	        sudo ./ZAP_2_11_1_unix.sh   
   
1.5-Click Next	

![image](https://user-images.githubusercontent.com/80597081/163392236-5d5d74e5-bfa9-479d-9228-21e53acb69e2.png)

1.6-Accept the agreement

![image](https://user-images.githubusercontent.com/80597081/163392254-63232db1-91e8-4240-9d36-15ac9b23854f.png)

1.7- Choose Standard installation

![image](https://user-images.githubusercontent.com/80597081/163392280-0021e527-cc7b-431d-8d91-0c4927885282.png)

1.8- If you see this, installation is successful. OWASP ZAP can be run successfully.

![image](https://user-images.githubusercontent.com/80597081/163392304-87b7fcb8-c291-455d-ad0f-f523b89d6ff9.png)

2-Install MongoDB

3-Install Java, NodeJS, npm 

After these steps, we can run the project.

To run Project:

First

    Run Java Spring Boot Application
    
Second

    cd src/main/ui
    
    npm start
    
