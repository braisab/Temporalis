# FASE DE IMPLANTACIÓN

## Manual técnico

### Información relativa á instalación

**- Requirimentos de hardware, servidores na nube, etc.**   
No relativo ao hardware, será válido calquera dispositivo con sistema Android.
Será necesario un servidor na nube para implantar a base de datos externa á cal se conectará a aplicación para facer todas as operacións que dependan de ela. O servidor escollido nun principio será un servidor <a href="https://www.ionos.es/server-configurator?__sendingdata=1&cart.action=add-bundle&cart.bundle=tariff-cloud-vps-fix-xxl-bundle&packageselection=servidores%2Fvps">VPS</a>, unha vez que o proxecto comeza a contar con moitos máis usuarios contratarase un servidor <a href="https://www.ionos.es/server-configurator?__sendingdata=1&cart.action=add-bundle&cart.bundle=tariff-ngcs-unlimited-fix-xxl-bundle&packageselection=cloud%2Fservidores-cloud">Cloud</a>.

**- Software necesario (S.O. válidos, software externo co que interaciona a nosa aplicación, etc.).**  
Para lanzar a aplicación necesitarase un dispositivo con SO Android 6.0 ou unha versión maior.  
Na fase de desenvolvemento usarase un equipo co SO ubuntu 20.04 LTS para o desenvolvemento co programa Android Studio instalado e co SXBD MySQL Server para realizar as probas sobre a base de datos.  
Ademais contaremos coa API JDBC para a conexión da aplicación coa BD dende o lado do cliente.  

**Configuración inicial seguridade: devasa, control usuarios, rede.**  
A seguridade estará xestionada polo lado do servidor contratado.  

**Carga inicial de datos na base de datos. Migración de datos xa existentes noutros formatos.**  
Non existe unha carga inicial de datos na base de datos.  Crearanse as taboas

**Usuarios do sistema. Usuarios da aplicación.**  
Existirá un usuario con permisos de administrador para poder xestionar a BD, os usuarios da aplicación nunca poderán acceder como administradores.  

TODO:(Configuracion da BBDD: FUsionar os dous anteriores)  

### Información relativa á administración do sistema, é dicir, tarefas que se deberán realizar unha vez que o sistema estea funcionando, como por exemplo

**Copias de seguridade do sistema.**
Realizarase unha copia de seguridade semanal do sistema na fase de desenvolvemento e poidamos así facer un backup de este se ocurre algún fallo.  

**Copias de seguridade da base de datos.**  
As copias de seguridade da BD estarán xestionadas pola empresa contratada do servidor na nube.   Copia de seguridade todos os dias as 3 da maña

### Información relativa ó mantemento do sistema

**Corrixir erros.**  
Durante a fase de produción seguiranse realizando test en base ao código da aplicación para buscar erros e poder correxilos.  

**Engadir novas funcionalidades.**  
Durante a fase de producción contemplase engadir nopvas funcionalidades á aplicación como poden ser:  

- Un **chat** en vivo co que se axilizaría a comunicación entre usuarios e estes non terían que poñer un correo-e ou un teléfono para poder contactar.
- Unha interface de **areas** nas que se poderían incluír e agrupar tanto ofertas como demandas e así axilizar a busca de estas.
- Un servizo de **notificacións**. Cada vez que se creé unha oferta ou unha demanda cada usuario podería recibir unha notificación co título de creación de esta.
- Un **calendario** interactivo no que os usuarios poderían acceder directamente aos días e abrir unha interface na que se verían as ofertas e demandas para ese día concreto. Este calendario tamén podería servir para crear ofertas/demandas directamente no día seleccionado.

**Adaptación por actualizacións de software e/ou hardware.**  
As actualizacións da aplicación están xestionadas pola Play Store de Google a cal busca actualizacións do software cada 24 horas polo que ao modificar a aplicación, a cal estará subida á Play Store esta actualizarase automáticamente en todos os dispositivos nos que se atope.

## Protección de datos de carácter persoal  
Os únicos datos de caracter persoal aos que accede a aplicación son o correo electrónico e o teléfono do usuario. De todas maneiras segundo a Lei Orgánica de protección de datos di, a modo de resume, que a aplicación debe:  
- Estipular os aspectos esenciais para a protección da privacidade, tales como o consentimento informado e previo do usuario.  
- Incluír o principio de anotación do propósito para a que se recolle a información.  
- Ten obrigación de informar correctamente os usuarios finais sobre os seus dereitos ou sobre os prazos de conservación de datos.
- Ao usuario que descarga unha aplicación debe dárselle a opción de poder cancelar esa instalación. E debe saber a que información vai acceder o programador con anterioridade á descarga.
-  As finalidades do tratamento deses datos teñen que estar ben explicadas e deben ser facilmente entendibles para un usuario non experto. Descartándose os cambios inesperados nas condicións do servizo.

Polo tanto unha vez se instala a app haberá que avisar ao usuario mediante unha pantalla.
