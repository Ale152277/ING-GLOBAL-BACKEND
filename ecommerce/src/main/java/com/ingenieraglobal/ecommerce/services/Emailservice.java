package com.ingenieraglobal.ecommerce.services;
import jakarta.mail.MessagingException;//gestiona fallos como problemas de conextion SMTP, direcciones incorrectas o errores en el formato de mensaje
import jakarta.mail.internet.MimeMessage; //API que sirve para crear, estructurar y enviar mensajes de corres complejos
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender; //Define metodos (send(mimemessage message)) funciona como un contrato, cualquiera que lo implementa tiene la misma forma de enviar correos
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class Emailservice {

    private final JavaMailSender mailSender;

    @Value("${app.mail.remitente}")
    private String remitente;
    

    @Value("${app.frontend.url}")
    private String forntendUrl;

    //constructor 
    public Emailservice(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }
/*
this.mailSender → se refiere a la propiedad de la clase
EmailSender → se refiere al parámetro temporal del constructor
*/


    public void enviarEmailVerificacion(String destinatario, String nombreCompleto, String token){
        try{
            MimeMessage message = mailSender.createMimeMessage(); //Permite crear correos con html, adjuntos, etc..
            
            //Clase que facilita la creacion de correos complejos
            //message ==> Es el message que acabamos de crear
            //true ==> indica que el mensaje puede tener contenido HTML y adjuntos 
            //UTF 8 ==> Usa codificacion de caracteres para soportar acentos, ñ, emojis, etc..
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); 
            
            helper.setFrom(remitente);//configura el remitente del correo
            helper.setTo(destinatario); //configura el destinatario del correo
            helper.setSubject("Verifica tu cuenta - Ingeniera Global"); //configura el asunto del correo

            
            //crea un link de verificacion completo
            //concatena el endpoint con el token unico (" https://miweb.com/verificar-email?token=abc123")
            String linkVerificacion = forntendUrl + "/verificar-email?token="+ token;
            

            //se llama al metodo buildVerificacion para generar el contenido HTML del correo
            //pasa el nombre del usuario y el link de verificacion
            String html = buildVerificacionHtml(nombreCompleto, linkVerificacion);
            

            //Configura el cuerpo del correo
            //html ==> contenido
            //true ==> indica que es HTML, si fuera false sería texto plano
            helper.setText(html, true);

            //enviar el correo usando el bean JavaMailSender
            //Spring se encarga de conectarse al servidor SMTP (GMAIL) y enviar el mensaje
            mailSender.send(message);

        }catch (MessagingException e) {
            throw new RuntimeException("Error al enviar email de verificación: " + e.getMessage(), e);
        }
    }

    public void enviarConsulta(String destinatario, String nombreUsuario, String emailUsuario, String asunto, String mensaje){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject("[CONSULTA WEB]" + asunto);
            helper.setText(buildConsultaHtml(nombreUsuario, emailUsuario, asunto, mensaje), true);

            mailSender.send(message);
            
        } catch (MessagingException e) {

            throw new RuntimeException("Error al enviar consulta"+ e.getMessage(), e);
        }
    }

    private String buildVerificacionHtml(String nombre, String link) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head><meta charset="UTF-8"/></head>
            <body style="font-family:Arial,sans-serif;background:#f4f4f4;margin:0;padding:20px;">
              <div style="max-width:560px;margin:auto;background:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.1);">

                <!-- Header -->
                <div style="background:#c0392b;padding:28px 32px;text-align:center;">
                  <h1 style="color:#fff;margin:0;font-size:20px;letter-spacing:1px;">INGENIERÍA GLOBAL</h1>
                  <p style="color:rgba(255,255,255,0.8);margin:4px 0 0;font-size:13px;">Proyectos Integrales</p>
                </div>

                <!-- Body -->
                <div style="padding:36px 32px;">
                  <h2 style="color:#222;margin-top:0;">¡Hola, %s! 👋</h2>
                  <p style="color:#555;line-height:1.7;">
                    Gracias por registrarte en nuestra tienda. Para activar tu cuenta y comenzar
                    a explorar nuestros productos, por favor verifica tu correo electrónico.
                  </p>

                  <div style="text-align:center;margin:32px 0;">
                    <a href="%s"
                       style="background:#c0392b;color:#fff;text-decoration:none;padding:14px 36px;
                              border-radius:4px;font-weight:bold;font-size:15px;letter-spacing:0.5px;
                              display:inline-block;">
                      VERIFICAR MI CUENTA
                    </a>
                  </div>

                  <p style="color:#888;font-size:13px;line-height:1.6;">
                    Este enlace es válido por <strong>24 horas</strong>.<br/>
                    Si no creaste esta cuenta, puedes ignorar este correo.
                  </p>

                  <hr style="border:none;border-top:1px solid #eee;margin:24px 0;"/>

                  <p style="color:#aaa;font-size:12px;">
                    Si el botón no funciona, copia y pega este enlace en tu navegador:<br/>
                    <a href="%s" style="color:#c0392b;word-break:break-all;">%s</a>
                  </p>
                </div>

                <!-- Footer -->
                <div style="background:#222;padding:16px 32px;text-align:center;">
                  <p style="color:#aaa;margin:0;font-size:12px;">
                    © 2025 Ingeniería Global Grupo IG — ingenieriaglobalperu.com
                  </p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(nombre, link, link, link);
    }

    private String buildConsultaHtml(String nombre, String email, String asunto, String mensaje) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head><meta charset="UTF-8"/></head>
            <body style="font-family:Arial,sans-serif;background:#f4f4f4;margin:0;padding:20px;">
              <div style="max-width:600px;margin:auto;background:#fff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                <div style="background:#c0392b;padding:24px 32px;">
                  <h1 style="color:#fff;margin:0;font-size:20px;">Nueva Consulta — Ingeniería Global</h1>
                </div>
                <div style="padding:32px;">
                  <table style="width:100%;border-collapse:collapse;">
                    <tr>
                      <td style="padding:10px 0;color:#666;font-weight:bold;width:130px;">👤 Nombre:</td>
                      <td style="padding:10px 0;color:#333;">%s</td>
                    </tr>
                    <tr style="background:#f9f9f9;">
                      <td style="padding:10px;color:#666;font-weight:bold;"> Email:</td>
                      <td style="padding:10px;color:#333;">%s</td>
                    </tr>
                    <tr>
                      <td style="padding:10px 0;color:#666;font-weight:bold;"> Asunto:</td>
                      <td style="padding:10px 0;color:#333;">%s</td>
                    </tr>
                  </table>
                  <div style="margin-top:24px;">
                    <p style="color:#666;font-weight:bold;margin-bottom:8px;"> Mensaje:</p>
                    <div style="background:#f9f9f9;border-left:4px solid #c0392b;padding:16px;border-radius:4px;color:#333;line-height:1.6;">
                      %s
                    </div>
                  </div>
                </div>
                <div style="background:#222;padding:16px 32px;text-align:center;">
                  <p style="color:#aaa;margin:0;font-size:12px;">Enviado desde ingenieriaglobalperu.com</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(nombre, email, asunto, mensaje.replace("\n", "<br/>"));
    }


    
}
