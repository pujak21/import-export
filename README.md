# 🌿 Turmexa Organic – Export Inquiry System

Turmexa Organic is a brand-focused export website built for **Kailash Overseas**, designed to handle bulk inquiries for organic products like Lakadong Turmeric and Moringa Powder.

This project includes a complete full-stack lead capture and email automation system.

---

## 🚀 Features

- 🌿 Brand-focused responsive frontend
- 📦 Product showcase (Lakadong Turmeric & Moringa)
- 📩 Bulk/Export inquiry form
- 🗄 MongoDB data storage
- 📧 Automated email notification to admin
- 📧 Automated confirmation email to customer
- 🔐 Gmail SMTP integration (App Password)
- 🔄 Smooth scroll navigation
- 🎨 Modern UI with structured sections

---

## 🛠 Tech Stack

### Frontend
- HTML
- CSS
- JavaScript (Fetch API)

### Backend
- Java
- Spring Boot
- Spring Mail (SMTP)

### Database
- MongoDB

---

## 🔁 System Flow

User submits inquiry form  
⬇  
Spring Boot API receives data  
⬇  
Data saved to MongoDB  
⬇  
Admin receives email notification  
⬇  
Customer receives confirmation email  

---



## 🔐 Email Configuration

Configured using Gmail SMTP:

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true


Uses Gmail App Password for secure authentication.