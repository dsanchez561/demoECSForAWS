# demoECSForAWS
Proyecto demo para desplegar por medio de AWS codepipeline a AWS ECS

Contiene un proyecto Java Web con el framework spring, el cual se puede correr de dos formas

- Localmente
- En Amazon Web Services, por medio de ECS, ECR y CodePipeline

Para ejecutarlo *localmente* se encuentra el archivo DockerFile, el cual va a crear un contenedor con Tomcat en donde el artefacto de la aplicación (demo-0.0.1-SNAPSHOT.war) se desplegará, se debe generar el artefacto con maven utilizando el comando *mvn clean install*

Para ejecutarlo en AWS se deben crear 3 stacks en el orden que se mencionan en cloud formation de la siguiente manera (Se da por hecho que ya existe una VPC creada)

# Stack 1: ClusterECS

Se encarga de crear el cluster en ECS y su instancia asociada

Se deben diligenciar los siguientes parámetros
1. Tipo de despliegue (Fargate, EC2) en nuestro caso será EC2 que se encuentra disponible en todas las zonas de AWS en el mundo.
2. Tipo de instancia: El tamaño de la instancia, por defecto será t2.micro
3. Tamaño del cluster: Tamaño del cluster y va relacionado al grupo de auto escalamiento, por defecto es 2
4. Subnets: Se da por entendido que ya se tienen creadas y se recomienda seleccionar minimo 2 (Tener presente que zonas se seleccionan, más adelante las vamos a necesitar)
5. Grupo de seguridad: Reglas de entrada y salida que se aplicarán al cluster que se creará
6. VPC: Se debe tener creada una y seleccionarla para que el cluster quede en la VPC


# Stack 2: ServiceECS

Se encarga de crear el servicio y la tarea que ejecutará la imagen que se encontrará en ECR (Ver Stack 3)

1. Grupo de seguridad: Reglas de entrada y salida que se aplicarán al servicio que correra en la instancia que se encuentra en el cluster previamente creado
2. Subnets: Se selecciona la zona de disponibilidad en la que quedo la EC2 del cluster o se seleccionan ambas zonas seleccionadas en la creación del anterior cluster


# Stack 3: PipelineAndCodeBuildECS

Se encarga de crear el Pipeline, con su respectivo CodeBuild (El cual leera el archivo de este repositorio llamado *buildspec.yml* en el que se encuentran los pasos de ejecución para crear la nueva versión de la imagen que se subira a ECR y se genera con el artefacto de la aplicación (demo-0.0.1-SNAPSHOT.war)

1. GitHubRepo: Nombre del repositorio de GIT el cual se desea desplegar (Debe tener un DockerFile y buildspec.yml en la raiz del proyecto), en este caso utilizaremos este demoECSForAWS
2. GitHubBranch: Rama de Git con la que se desea trabajar
3. GitHubToken: Token para la autenticación a GIT, para la creación ir a https://github.com/settings/tokens
4. GitHubUser: Usuario dueño del repositorio, en este caso utilizaremos dsanchez561 

Finalmente, tenemos todo lo necesesario para hacer el test de la aplicación desplegada, para verificar se debe ir a las instancias EC2 y buscar la instancia con el nombre "<Nombre del stack #1> - ECS Host", una vez localizada se copia el endpoint de esa instancia y se busca en el navegador de la siguiente manera http://<Endpoint-EC2>:8080/demo-0.0.1-SNAPSHOT/public/test

Si quedó todo bien desplegado veremos el siguiente mensaje

{ "value" : "Pagina publica funcionando"}

END...
