package org.fundoonotes.service;

import jakarta.mail.internet.MimeMessage;
import org.fundoonotes.model.Note;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;

//import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

    @Service
    public class EmailServiceImpl implements EmailService {

        @Autowired
        private JavaMailSender mailSender;

        @Value("${MAIL_FROM}")
        private String fromEmail;

        @Value("${BREVO_API_KEY}")
        private String brevoApiKey;

//        @Override
//        public void sendOtp(String to, String otp) {
//
//            try {
//
//                MimeMessage message = mailSender.createMimeMessage();
//
////                MimeMessageHelper helper = new MimeMessageHelper(message, true);
//                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//                helper.setFrom(fromEmail);
//                helper.setTo(to);
//
//                helper.setSubject("Fundoo Notes - Password Reset OTP");
//
//                String html = """
//<!DOCTYPE html>
//<html>
//
//<head>
//<meta charset="UTF-8">
//<title>Password Reset OTP</title>
//</head>
//
//<body style="
//margin:0;
//padding:40px;
//background:#f3f4f6;
//font-family:Arial,Helvetica,sans-serif;
//">
//
//<table width="100%%" cellpadding="0" cellspacing="0">
//<tr>
//<td align="center">
//
//<table width="600" cellpadding="0" cellspacing="0"
//style="
//background:#ffffff;
//border-radius:12px;
//overflow:hidden;
//box-shadow:0 4px 15px rgba(0,0,0,0.08);
//">
//
//<!-- Header -->
//
//<tr>
//<td align="center"
//style="
//background:#4285F4;
//padding:20px;
//font-size:24px;
//font-weight:bold;
//color:white;
//">
//
//Fundoo Notes
//
//</td>
//</tr>
//
//<!-- Title -->
//
//<tr>
//<td align="center"
//style="
//padding:28px 35px 10px;
//font-size:24px;
//font-weight:bold;
//color:#202124;
//">
//
//Password Reset OTP
//
//</td>
//</tr>
//
//<!-- Description -->
//
//<tr>
//<td align="center"
//style="
//padding:0 40px;
//font-size:15px;
//color:#5f6368;
//line-height:24px;
//">
//
//Use the following One-Time Password (OTP) to reset your
//Fundoo Notes password.
//
//
//</td>
//</tr>
//
//<!-- OTP Box -->
//
//<tr>
//<td align="center"
//style="padding:28px;">
//
//<div
//style="
//display:inline-block;
//padding:18px 40px;
//background:#f8f9fa;
//border:2px dashed #4285F4;
//border-radius:10px;
//font-size:34px;
//font-weight:bold;
//letter-spacing:8px;
//color:#202124;
//">
//
//%s
//
//</div>
//
//</td>
//</tr>
//
//<!-- Info -->
//
//<tr>
//<td
//align="center"
//style="
//padding:0 40px 24px;
//font-size:14px;
//color:#5f6368;
//line-height:22px;
//">
//
//This OTP is valid for <b>5 minutes</b>.
//
//Do not share this code with anyone.
//
//</td>
//</tr>
//
//<!-- Footer -->
//
//<tr>
//<td
//style="
//background:#f8f9fa;
//padding:18px;
//text-align:center;
//font-size:13px;
//color:#6b7280;
//line-height:20px;
//">
//
//If you didn't request a password reset,
//you can safely ignore this email.
//
//<br><br>
//
//&copy; 2026 Fundoo Notes
//
//</td>
//</tr>
//
//</table>
//
//</td>
//</tr>
//</table>
//
//</body>
//</html>
//""".formatted(otp);
//
//                helper.setText(html, true);
//
//                mailSender.send(message);
//
//                System.out.println("Mail Sent Successfully to " + to);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("========== OTP MAIL ERROR ==========");
//                System.out.println("TO: " + to);
//                System.out.println("FROM: " + fromEmail);
//                System.out.println("ERROR: " + e.getMessage());
//                System.out.println("====================================");
//                throw new RuntimeException("Failed to send OTP email", e);
//
//            }
//
//        }
@Override
public void sendOtp(String to, String otp) {

    try {
        String html = """
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>Password Reset OTP</title>
</head>

<body style="
margin:0;
padding:40px;
background:#f3f4f6;
font-family:Arial,Helvetica,sans-serif;
">

<table width="100%%" cellpadding="0" cellspacing="0">
<tr>
<td align="center">

<table width="600" cellpadding="0" cellspacing="0"
style="
background:#ffffff;
border-radius:12px;
overflow:hidden;
box-shadow:0 4px 15px rgba(0,0,0,0.08);
">

<tr>
<td align="center"
style="
background:#4285F4;
padding:20px;
font-size:24px;
font-weight:bold;
color:white;
">
Fundoo Notes
</td>
</tr>

<tr>
<td align="center"
style="
padding:28px 35px 10px;
font-size:24px;
font-weight:bold;
color:#202124;
">
Password Reset OTP
</td>
</tr>

<tr>
<td align="center"
style="
padding:0 40px;
font-size:15px;
color:#5f6368;
line-height:24px;
">
Use the following One-Time Password (OTP) to reset your
Fundoo Notes password.
</td>
</tr>

<tr>
<td align="center"
style="padding:28px;">

<div
style="
display:inline-block;
padding:18px 40px;
background:#f8f9fa;
border:2px dashed #4285F4;
border-radius:10px;
font-size:34px;
font-weight:bold;
letter-spacing:8px;
color:#202124;
">
%s
</div>

</td>
</tr>

<tr>
<td
align="center"
style="
padding:0 40px 24px;
font-size:14px;
color:#5f6368;
line-height:22px;
">
This OTP is valid for <b>5 minutes</b>.
Do not share this code with anyone.
</td>
</tr>

<tr>
<td
style="
background:#f8f9fa;
padding:18px;
text-align:center;
font-size:13px;
color:#6b7280;
line-height:20px;
">
If you didn't request a password reset,
you can safely ignore this email.
<br><br>
&copy; 2026 Fundoo Notes
</td>
</tr>

</table>

</td>
</tr>
</table>

</body>
</html>
""".formatted(otp);

        // Escape HTML for JSON
        String safeHtml = html
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");

        String jsonBody = """
                {
                  "sender": {
                    "name": "Fundoo Notes",
                    "email": "%s"
                  },
                  "to": [
                    { "email": "%s" }
                  ],
                  "subject": "Fundoo Notes - Password Reset OTP",
                  "htmlContent": "%s"
                }
                """.formatted(fromEmail, to, safeHtml);

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("api-key", brevoApiKey)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        java.net.http.HttpResponse<String> response =
                java.net.http.HttpClient.newHttpClient()
                        .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            System.out.println("Mail Sent Successfully to " + to);
        } else {
            System.out.println("========== OTP MAIL ERROR ==========");
            System.out.println("TO: " + to);
            System.out.println("FROM: " + fromEmail);
            System.out.println("STATUS: " + response.statusCode());
            System.out.println("BODY: " + response.body());
            System.out.println("====================================");
            throw new RuntimeException("Failed to send OTP email");
        }

    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("========== OTP MAIL ERROR ==========");
        System.out.println("TO: " + to);
        System.out.println("FROM: " + fromEmail);
        System.out.println("ERROR: " + e.getMessage());
        System.out.println("====================================");
        throw new RuntimeException("Failed to send OTP email", e);
    }
}

    @Override
    public void sendReminderMail(String to, Note note) {

            try {
//                MimeMessage message = mailSender.createMimeMessage();
//
//                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//                helper.setFrom(fromEmail);
//                helper.setTo(to);
//
//                helper.setSubject("Fundoo Notes Reminder");
                String html = """
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>Fundoo Notes Reminder</title>
</head>

<body style="
margin:0;
padding:35px;
background:#f3f4f6;
font-family:Arial,Helvetica,sans-serif;
">

<table width="100%%" cellpadding="0" cellspacing="0">
<tr>
<td align="center">

<table width="620" cellpadding="0" cellspacing="0"
style="
background:#ffffff;
border-radius:12px;
overflow:hidden;
box-shadow:0 4px 15px rgba(0,0,0,0.08);
">

<!-- Header -->

<tr>
<td align="center"
style="
background:#4f8df7;
padding:22px;
color:white;
font-size:26px;
font-weight:bold;
">

🔔 Fundoo Notes

</td>
</tr>

<!-- Title -->

<tr>
<td align="center"
style="
padding:28px 40px 10px;
font-size:26px;
font-weight:bold;
color:#202124;
">

Reminder Notification

</td>
</tr>

<!-- Description -->

<tr>
<td align="center"
style="
padding:0 50px;
font-size:15px;
color:#5f6368;
line-height:26px;
">

This is a reminder for one of your notes.
Don't forget to review it.

</td>
</tr>

<!-- Note Card -->

<tr>
<td style="padding:28px 50px;">

<table width="100%%"
style="
border:1px solid #e5e7eb;
border-radius:10px;
background:#fafafa;
">

<tr>
<td style="
padding:14px 18px 4px;
font-size:13px;
font-weight:bold;
color:#6b7280;
letter-spacing:1px;
">
NOTE TITLE
</td>
</tr>

<tr>
<td style="
padding:0 18px 14px;
font-size:21px;
font-weight:bold;
color:#202124;
line-height:28px;
">
%s
</td>
</tr>

<tr>
<td style="
padding:0 18px;
font-size:13px;
font-weight:bold;
color:#6b7280;
letter-spacing:1px;
">

DESCRIPTION

</td>
</tr>

<tr>
<td style="
padding:0 18px 18px;
font-size:15px;
color:#444;
line-height:24px;
">

%s

</td>
</tr>

<tr>
<td style="
padding:0 18px;
font-size:13px;
font-weight:bold;
color:#6b7280;
letter-spacing:1px;
">

REMINDER TIME

</td>
</tr>

<tr>
<td style="
padding:0 18px 22px;
font-size:15px;
font-weight:bold;
color:#202124;
">

%s

</td>
</tr>

</table>

</td>
</tr>

<!-- Button -->

<tr>
<td align="center"
style="padding:10px 40px 32px;">

<a
href="http://localhost:4200/login"
style="
background:#4f8df7;
color:white;
text-decoration:none;
padding:13px 36px;
border-radius:8px;
font-size:15px;
font-weight:bold;
display:inline-block;
">

Open Fundoo Notes

</a>

</td>
</tr>

<!-- Footer -->

<tr>
<td
style="
background:#f8f9fa;
padding:22px;
text-align:center;
color:#6b7280;
font-size:13px;
line-height:22px;
">

This reminder was generated from your
<b>Fundoo Notes</b> account.

<br><br>

© 2026 Fundoo Notes • Stay Organized • Never Miss Anything

</td>
</tr>

</table>

</td>
</tr>
</table>

</body>
</html>
""".formatted(
                        note.getTitle(),
                        note.getDescription(),
                        note.getReminderTime()
                );
//                helper.setText(html, true);
//                mailSender.send(message);

                // SMTP mat use karo — Brevo HTTPS API
                sendViaBrevo(to, "Fundoo Notes Reminder", html);

                System.out.println("Reminder Mail Sent Successfully");

            } catch (Exception e) {

                System.out.println("Reminder Mail Failed");

                e.printStackTrace();

            }
        }

        @Override
        public void sendCollaboratorMail(
                String to,
                String ownerName,
                String ownerEmail,
                String noteTitle) {

            try {
//                MimeMessage message = mailSender.createMimeMessage();
//
//                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//                helper.setFrom(fromEmail);
//                helper.setTo(to);
//
//                helper.setSubject("A note has been shared with you");
                String html = """
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>Fundoo Notes</title>
</head>

<body style="
margin:0;
padding:40px;
background:#f3f4f6;
font-family:Arial,Helvetica,sans-serif;
">

<table width="100%%" cellpadding="0" cellspacing="0">
<tr>
<td align="center">

<table width="620" cellpadding="0" cellspacing="0"
style="
background:#ffffff;
border-radius:12px;
overflow:hidden;
box-shadow:0 4px 15px rgba(0,0,0,0.08);
">

<!-- Header -->

<tr>
<td align="center"
style="
background:#4285F4;
padding:20px;
color:white;
font-size:24px;
font-weight:bold;
">

📒 Fundoo Notes

</td>
</tr>

<!-- Greeting -->

<tr>
<td align="center"
style="
padding:24px 35px 8px;
font-size:25px;
font-weight:bold;
color:#202124;
">

👋 A note has been shared with you

</td>
</tr>

<!-- Description -->

<tr>
<td align="center"
style="
padding:0 40px;
font-size:16px;
color:#5f6368;
line-height:24px;
">

<b style="color:#202124;">%s</b>

(<a href="mailto:%s"
style="
color:#1a73e8;
text-decoration:none;
">
%s
</a>)

has invited you to collaborate on a note.

</td>
</tr>

<!-- Note Card -->

<tr>
<td style="padding:22px 40px 16px;;">

<table width="100%%"
style="
border:1px solid #e5e7eb;
border-radius:10px;
background:#fafafa;
">

<tr>
<td
style="
padding:14px;
font-size:12px;
color:#6b7280;
font-weight:bold;
letter-spacing:1px;
">

NOTE TITLE

</td>
</tr>

<tr>
<td
style="
padding:0 14px 14px;
font-size:20px;
font-weight:bold;
color:#202124;
">

%s

</td>
</tr>

</table>

</td>
</tr>


<!-- Button -->

<tr>

<td
align="center"
style="padding:18px 40px 24px;">

<a
href="http://localhost:4200/login"
style="
background:#1a73e8;
color:white;
text-decoration:none;
padding:12px 34px;
font-size:15px;
border-radius:6px;
font-weight:bold;
display:inline-block;
">

Open Note

</a>

</td>

</tr>

<!-- Footer -->

<tr>

<td
style="
background:#f8f9fa;
text-align:center;
color:#6b7280;
padding:18px;
font-size:13px;
line-height:20px;
">

You received this email because
<b>%s</b> shared a note with you using
<b>Fundoo Notes</b>.

<br><br>

© 2026 Fundoo Notes • Read • Edit • Collaborate

</td>

</tr>

</table>

</td>
</tr>
</table>

</body>
</html>
""".formatted(

                        ownerName,
                        ownerEmail,
                        ownerEmail,
                        noteTitle,
                        ownerName,
                        ownerEmail,
                        ownerName

                );
//                helper.setText(html, true);
//                mailSender.send(message);
                // SMTP mat use karo — Brevo HTTPS API
                sendViaBrevo(to, "A note has been shared with you", html);

                System.out.println("Collaborator Mail Sent");

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        /**
         * Common Brevo API sender.
         * File ke END pe — class ke last closing brace } se pehle.
         */
        private void sendViaBrevo(String to, String subject, String html) {
            try {
                String safeHtml = html
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "");

                String jsonBody = """
                        {
                          "sender": {
                            "name": "Fundoo Notes",
                            "email": "%s"
                          },
                          "to": [
                            { "email": "%s" }
                          ],
                          "subject": "%s",
                          "htmlContent": "%s"
                        }
                        """.formatted(fromEmail, to, subject, safeHtml);

                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .uri(java.net.URI.create("https://api.brevo.com/v3/smtp/email"))
                        .header("accept", "application/json")
                        .header("content-type", "application/json")
                        .header("api-key", brevoApiKey)
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                java.net.http.HttpResponse<String> response =
                        java.net.http.HttpClient.newHttpClient()
                                .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    System.out.println("========== BREVO MAIL ERROR ==========");
                    System.out.println("TO: " + to);
                    System.out.println("SUBJECT: " + subject);
                    System.out.println("STATUS: " + response.statusCode());
                    System.out.println("BODY: " + response.body());
                    System.out.println("======================================");
                    throw new RuntimeException("Failed to send email via Brevo");
                }

                System.out.println("Mail Sent Successfully to " + to + " | " + subject);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to send email via Brevo", e);
            }
        }
    }