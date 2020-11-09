# REQUIRIMENTOS DO SISTEMA

Este documento describe os requirimentos para *Temporis* (nome provisional) especificando que funcionalidade ofrecerá e de que xeito.

## Descrición Xeral

O proxecto basease nun banco de tempo dentro do concello de Santiago de Compostela.  

Esta aplicación servirá para poñer en relación ás diferentes persoas que desexen formar parte deste tipo de iniciativa e para facilitar a xestión do propio banco do tempo.

A idea principal da app é que, partindo de un "crédito" inicial de 10 horas  nas súa "contas de tempo", os usuarios poderán crear "ofertas/demandas" de servizos, habilidades, coñecementos..., nas cales outros usuarios poderán solicitar empregar o seu tempo, co cal se realizarán as transacción entre usuarios.

É dicir, a finalidade é usar o tempo como "moeda de cambio" para que así as persoas poidan satisfacer necesidades puntuais, expandir coñecementos en certos ámbitos, acompañarse...; ao fin e ao cabo, mellorar o benestar persoal e social.

## Funcionalidades


 1. Existirá un apartado onde o novo usuario deberá rexistrar o seu nome de usuario e contrasinal para o posterior login; tamén se introducirán un correo de contacto, ou se prefire un teléfono, para que o resto de usuarios poidan comunicarse con esta persoa; un perfil de competencias e intereses e unha zona xeográfica xa que non é o mesmo residir por exemplo en Aríns que na propia cidade de Santiago a efectos de mobilidade dos usuarios. Unha vez se faga o rexistro do novo usuario este poderá acceder á aplicación mediante un login  co cal se comprobará na base de datos se existe o usuario e o contrasinal asociado.  
 Os novos usuarios terán un "saldo" de tempo de 10 horas iniciais para poder comezar a empregar o banco de tempo. A contía inicial podería ser de 0 horas, pero considero que é importante que os posibles novos usuarios poidan acceder as ofertas dende un primeiro momento para así non perder a oportunidade de gañar un novo membro que participe activamente. Esas 10 horas serán introducidas na base de datos unha vez que se rexistre o novo usuario.  
 Se un usuario "gasta" todas as horas da súa conta, so poderá acceder ás demandas de outros usuarios. No relativo ás ofertas non poderá acceder ás dos outros usuarios pero si as que este oferte, co fin de poder xestionalas. Isto farase co fin de que o usuario que se quede sen tempo para gastar teña a opción de poder "gañar" horas.  

 2. Os usuarios contarán con unha interface propia na que poderán actualizar algún dos seus datos xa existentes, poñer unha imaxe se o desexan e nesta interface é onde poderán ver a cantidade de tempo da que dispoñen(en horas).


 3. Se un usuario quere ofertar un servizo, este contará con un botón para tal uso, o cal unha vez presionada abrirase unha nova pantalla onde se  poderá escribir un título da oferta, unha descrición de esta, poderá seleccionar unha zona xeográfica a onde os usuarios interesados poderán acudir, unha data e hora do servizo, e poderá limitar,se o desexa, a cantidade de persoas que poderán aceptar este servizo. Unha vez que remate e o usuario prema o botón "Ofertar", o servizo aparecerá na pantalla principal de ofertas. Se a cantidade de persoas que poden acceder a unha oferta chega ao seu límite esta desaparecerá da pantalla das ofertas. Tamén existirá un botón para cancelar a oferta se así o decide o usuario creador de esta.
 Un usuario poderá consultar todas as ofertas activas que el creou nunha pantalla diferente destinada a axilizar a busca de estas, para así poder modificar calquera aspecto da oferta concreta que desexe.

 4. Se un usuario quere crear unha demanda, este contará con un botón para tal uso, o cal unha vez presionado abrirase unha nova pantalla onde se poderá escribir un título da demanda, unha descrición de esta, unha zona xeográfica a onde os usuarios interesados poderán acudir, unha data e hora do demandado, e poderá limitar, se o desexa, a cantidade de persoas que poderán aceptar esta demanda. Unha vez que remate e o usuario prema o botón "Demandar", o servizo aparecerá na pantalla principal das demandas. Se a cantidade de persoas que poden acceder a unha demanda chega ao seu límite esta desaparecerá da pantalla das demandas. Tamén existirá un botón para cancelar a demanda se así o decide o usuario creador de esta.
 Un usuario poderá consultar todas as demandas activas que el creara anteriormente nunha pantalla diferente destinada a axilizar a busca de estas, para así poder modificar calquera aspecto da demanda concreta que desexe.


 5. Cando a un usuario lle interesase unha oferta ou unha demanda de outro usuario, o primeiro poderá presionar en ela para poder acceder a outra pantalla onde se detallará máis este servizo con datos de interese como unha descrición máis específica do propio, a data e a hora do servizo, a zona xeográfica a onde haberá que acudir e o nome do usuario que puxo este servizo. Ademais, nesta pantalla existirá un botón para que os usuarios interesados poidan avisar ao usuario que creou este servizo. Cando se prema este botón aparecerá na pantalla do usuario que o premeu o correo-e ou teléfono de contacto do usuario que creou o servizo, e a este chegaralle unha notificación co nome de usuario e correo ou teléfono de contacto (dependendo do que introducira no rexistro) da persoa interesada. Nesta pantalla existirá tamén un botón para cancelar a "contratación" do servizo se o usuario así o decide.
 Existirán dentro da app dúas pantalla nas que poderemos ver as ofertas e demandas, respectivamente, que foron aceptadas polo usuario, para poder consultar máis axilmente cales foron as ofertas e demandas que decidiu contratar e non ter que acudir as pantallas principais de oferta ou demanda e buscar unha por unha.  
 Se un usuario quere "aceptar" unha oferta ou demanda que se solape na data e horas de unha xa aceptada posteriormente, será imposible aceptar a segunda oferta ou demanda sempre e cando non se cancele a primeira.

 6. Cando finalice o tempo na data establecida, aparecerá no servizo ofertado ou demandado outro botón para facer a "transacción" pola cal se lle sumará e restará tempo aos usuarios implicados. Ao igual que nunha transacción real será o usuario que ten que "pagar" o tempo establecido o que realice a operación mediante este botón. Se o servizo se trata de unha oferta, serán as persoas que "contraten" esa oferta os que contarán co botón citado para poder dar o seu tempo. No caso de ser unha demanda, será o usuario creador de esa demanda o que lle aparecerá o botón coa finalidade de "pagar" polo servizo realizado aos usuarios que aceptasen esa demanda.


## Tipos de usuarios

O tipo de usuario vai a ser o mesmo en todo momento.

O único que podemos diferenciar é o rol que terá o usuario en función de se este crea a oferta ou demanda ou se este vai facer uso de esa oferta ou demanda.

O usuario que crea un servizo terá un rol de "administrador do servizo", xa que poderá modificalo ou eliminalo dentro da app, o que significa crealo, modificalo ou eliminalo dentro da base de datos nas táboas referentes ás ofertas e as demandas.

O usuario que fai uso dun servizo terá o rol de "usuario do servizo", o cal non poderá facer nada máis que visualizar os datos propios de este servizo e se así o decide aceptar empregalo e tamén, posteriormente a aceptar un servizo, se así o decide, cancelar empregalo.

## Avaliación da viabilidade técnica do proxecto

### Hardware requirido

Para este punto hai que ter en conta as fases do proxecto, o cal contará con dúas: a fase de desenvolvemento e a fase de produción.

- Na fase de desenvolvemento o hardware requirido será un ordenador para poder desenvolver a aplicación por parte do programador/es e estes poderán empregar un ou varios smartphones para facer as probas necesarias.

- Na fase de produción lanzarase a aplicación para o uso de esta por parte dos usuarios. Nesta fase os usuarios que queiran empregar a aplicación precisarán un smartphone. Nesta fase, sobre todo ao principio, os programadores deberán revisar os erros que poida dar a aplicación e que non foran contemplados na fase anterior, polo que se seguira necesitando un ordenador para poder arranxar estes erros.

### Software

Centrándonos nas dúas fases anteriores:

- Na fase de produción os programadores precisarán a última versión do IDE Android Studio, neste caso a 4.0. Aínda que nun principio non fará falla, xa que con este IDE se poden crear máquinas virtuais que simulan todas as versións de Android necesarias, sería convinte, como dixen antes, contar con varios smartphones, con sistema operativo android o cal deberá ser a versión 6.0 en adiante, debido a que o desenvolvemento das aplicacións farase máis fácil, sobre todo no relativo aos permisos que a nosa app poida precisar.
A linguaxe de programación que se empregará para desenvolver a aplicación será Java na versión 8.0.  
Nesta fase empregarase MySQL 5.5 como sistema de xestión de base de datos para aloxar os datos da app. Dado que nesta fase as probas se farán nun entorno local a base de datos aloxarase no ordenador propio e por este motivo empregaremos o MySQL. Na seguinte fase, aínda que os datos estarán aloxados na nube, seguirase empregando o entorno local para facer probas sobre a corrección de erros que poidan aparecer.

- Na fase de produción, como xa dixen anteriormente, lanzarase a aplicación para o emprego de esta por parte dos usuarios e isto farase tanto na Play Store de google como en algún sistema para descargar aplicacións de código aberto como pode ser F-Droid. Os usuarios que queiran empregar a aplicación precisarán un smartphone que teña o sistema operativo Android na versión 6.0 en adiante.

## Interfaces externos

A aplicación non contará con ningunha interface de hardware. No relativo ás interfaces software, a app contará con varias pantallas coas que poderán interactuar os usuarios.

### Interfaces software

- Interface de **login do usuario** cos seguintes elementos:
    - 2 *EditText* (nome de usuario e contrasinal)
    - 1 *Botón* para acceder a aplicación.
    - 1 *TextView* oculto que apareza se o usuario non pon ben o seu nome de usuario ou o seu isto
    - 1 *TextView* interactivo para acceder a pantalla de rexistro.


- Interface de **rexistro do usuario** cos seguintes elementos:
    - 4 *EditText* (nome de usuario, contrasinal, repetir contrasinal, perfil de intereses e destrezas).
    - 2 *Editext* máis dos cales un sería obrigatorio cubrir(correo-e, teléfono)
    - 2 *Checkbox* para escoller un dos datos de contacto anteriores.
    - 1 *Spinner* co contido das areas xeográficas do concello para escoller unha.
    - 1 *Botón* para rexistrarse
    - 1 *TextView* oculto para dar un aviso se o nome de usuario xa existise.


- Interface de **ofertas** e interface de **demandas**:
  - As dúas interfaces contarán con un *ListView* o cal cargará as ofertas e demandas dende a base de datos nas respectivas interfaces.
  - 1 *Botón* que lance outra interface onde o usuario poida crear ofertas e demandas dependendo da interface na que se atope.


- Interface de **creación/modificación de oferta e demanda** (Interface de "Administrador do servizo"):
  - 6 *EditText* (Título da oferta/demanda, descrición da oferta/demanda, día, mes, hora, minuto).
  - 1 *Spinner* coa area xeográfica a onde acudir se é o caso.
  - 1 *Botón* "Crear" a oferta/demanda a cal pasará a formar parte de unha das interfaces anteriores. Unha vez creada a oferta/demanda este botón cambiará a "Cancelar" por se o usuario decide borrala.
  - 1 *Botón* "Realizar transacción" no caso da **interface de demanda** o cal se atopará oculto e so aparecerá cando pase a data e a hora indicadas na demanda.


- Interfaces de **visualización da oferta/demanda** (Interface de "Usuario do servizo"):
  - 8 ou 9 *TextView* (Título da oferta/demanda, descrición da oferta/demanda, día, mes, hora, minuto, coa area xeográfica a onde acudir, correo-e ou telefono, do ofertante/demandante, ou ambos; estos últimos so aparecerán se o usuario interesado preme no botón "Aceptar").
  - 1 *Botón* para "Aceptar" ou "Decidir acudir" á oferta/demanda. Unha vez aceptada a oferta/demanda este botón cambiará a "Cancelar" por se o usuario decide non acudir.
  - 1 *Botón* "Realizar transacción" no caso da **interface de oferta** o cal se atopará oculto e so aparecerá cando pase a data e a hora indicadas na oferta.


- Interfaces de ""**Ofertas creadas**", "**Demandas creadas**":
    - 1 *ListView* coas ofertas/demandas creadas polo propio usuario, se premes nun item do ListView este abrirá a interface da creación/modificación de oferta e demanda


- Interfaces de "**Ofertas solicitadas**", "**Demandas solicitadas**":
    - 1 *ListView* coas ofertas/demandas solicitadas polo usuario, se premes nun item do ListView este abrirá a interface da visualización de oferta e demanda


- Interface de **perfil de usuario**:
    - 5 *EditText* (nome usuario, contrasinal, perfil de interese e destrezas, correo-e e teléfono) co fin de que o usuario poida modificar os seus datos.
    - 1 *ImageView* na que poñer unha foto se así o desexa o usuario.
    - 1 *Botón* para aceptar as modificacións no caso de existir.

En todas as interfaces exceptuando a de **login** e a de **rexistro** existirá unha *action bar* que conterá o nome e unha imaxe do usuario un menú para poder abrir:  

  - Interface de **Ofertas**  
  - Interface de **Demandas**
  - Interface de **Ofertas creadas**
  - Interface de **Demandas creadas**
  - Interface de **Perfil de Usuario**

Durante o desenvolvemento da aplicación poderían ir xurdindo vistas menos relevantes dentro de cada unha das interfaces como poden ser imaxes para o background por exemplo.

## Análise de riscos e interesados

Os impactos positivos recaen directamente sobre as persoas que van a empregar a aplicación, dado que se prioriza o intercambio de tempo e a solidariedade entre os cidadáns, desbotando os intercambios monetarios, polo que esta aplicación axuda a poñer en valor as persoas.

Un posible risco que pode aparecer é que o usuario que ten que "pagar" as horas non o faga. Pero como ao fin e ao cabo esta é unha iniciativa solidaria e xa que esta trata de que as persoas se axuden entre sí, nun principio confiaremos no bo uso que se lle dará á aplicación. Se isto resultase un problema nun futuro, habería que tomar algunha medida como pode ser a automatización das "transaccións" unha vez que o servizo fose finalizado na data e na hora indicadas.

Outro posible risco é que un usuario que se quedara sen horas, o cal implica que non poida acceder ás ofertas de outros usuarios, intentase volver a rexistrarse con outro nome de usuario e outro contrasinal para reiniciar a conta a 10 horas. Neste caso podemos comprobar se o correo-e ou o teléfono introducido anteriormente por esta persoa xa existen na base de datos e se é así a aplicación non deixaría rexistrarse a este usuario.

## Actividades

Como xa se comentou anteriormente, o proxecto contará con 2 fases principais, as cales a súa vez se dividirán en varias fases:

  - A primeira fase ou fase de **desenvolvemento da aplicación**, na cal se ira dende a creación da entidade-relacional para a posterior implantación da base de datos, creación de diagrama de clases, desenvolvemento da aplicación (funcionalidade e interfaces), creación de tests co fin de atopar erros no código...

  - A segunda fase ou fase de **produción da aplicación** é na que se lanzará a aplicación para que os usuarios a poidan empregar. Nesta fase haberá que seguir optimizando a aplicación buscando erros e bugs a base de tests e de experiencias de usuarios.
  É en esta fase cando se proporán melloras futuras e ,se é posible, se irán implantando.

## Melloras futuras

Coas funcionalidades principais a aplicación estaría optimizada para poder lanzala na fase de produción. Nesta fase proporía certas melloras como:

  - Un **calendario** interactivo no que os usuarios poderían acceder directamente aos días e abrir unha interface na que se verían as ofertas e demandas para ese día concreto. Este calendario tamén podería servir para crear ofertas/demandas directamente no día seleccionado.

  - Un **chat** en vivo co que se axilizaría a comunicación entre usuarios e estes non terían que poñer un correo-e ou un teléfono para poder contactar uns cos outros.

  - Unha interface de **areas** nas que se poderían incluír e agrupar tanto ofertas como demandas e así axilizar a busca de estas.

  - Un servizo de **notificacións**. Cada vez que se vaia a crear unha oferta ou unha demanda cada usuario podería recibir unha notificación co título de creación de esta.
