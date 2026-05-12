# 📋 GUÍA PASO A PASO: PostgreSQL + DBeaver + data.sql

## Opción 2 - Base de Datos Local sin Docker

---

## ✅ PASO 1: Instalar PostgreSQL

### 1.1 Descargar PostgreSQL
- Ve a: https://www.postgresql.org/download/windows/
- Descarga la última versión estable para Windows

### 1.2 Instalar PostgreSQL
1. Ejecuta el archivo `.exe` descargado
2. Sigue el instalador con estos datos:
   - **Contraseña de `postgres`**: `postgres123` (IMPORTANTE: guárdalo)
   - **Puerto**: `5432` (default, no cambiar)
   - **Locale**: Spanish
   - **Stack Builder**: puedes hacer skip al final

### 1.3 Verificar la instalación
Abre PowerShell y ejecuta:
```powershell
psql --version
```
Deberías ver algo como: `psql (PostgreSQL) 15.x`

---

## ✅ PASO 2: Crear la Base de Datos en PostgreSQL

### 2.1 Conectarse a PostgreSQL
Abre PowerShell como Administrador y ejecuta:

```powershell
psql -U postgres
```

Te pedirá la contraseña que pusiste (postgres123)

### 2.2 Crear la BD
Dentro de psql, ejecuta estos comandos:

```sql
CREATE DATABASE proyecto_comida;
\l
\q
```

**Qué significa:**
- `CREATE DATABASE proyecto_comida;` → Crea la BD
- `\l` → Lista todas las BDs (verifica que se creó)
- `\q` → Sale de psql

---

## ✅ PASO 3: Configurar el Proyecto Spring Boot

### 3.1 Cambios ya realizados en `application.properties`
✅ **YA HECHO** - El archivo fue modificado para usar PostgreSQL

Verifica que tenga esto:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/proyecto_comida
spring.datasource.username=postgres
spring.datasource.password=postgres123
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
```

### 3.2 Cambios ya realizados en `pom.xml`
✅ **YA HECHO** - PostgreSQL driver fue añadido

Verifica que tenga esto en `<dependencies>`:
```xml
<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## ✅ PASO 4: Descargar e Instalar DBeaver

### 4.1 Descargar DBeaver
- Ve a: https://dbeaver.io/download/
- Descarga "Instalador Windows" (DBeaver Community es gratis)

### 4.2 Instalar DBeaver
- Ejecuta el instalador `.exe`
- Sigue los pasos por defecto hasta terminar

---

## ✅ PASO 5: Conectar DBeaver a PostgreSQL

### 5.1 Abrir DBeaver
- Inicia la aplicación DBeaver

### 5.2 Crear Nueva Conexión
1. En el menú superior: **Database** → **New Database Connection**
2. Selecciona **PostgreSQL**
3. Click en **Next**

### 5.3 Configurar la Conexión
Llena los campos así:

| Campo | Valor |
|-------|-------|
| **Server Host** | localhost |
| **Port** | 5432 |
| **Database** | proyecto_comida |
| **Username** | postgres |
| **Password** | postgres123 |
| **Save password locally** | ✅ (marcar) |

### 5.4 Probar la Conexión
- Click en el botón **Test Connection**
- Si sale verde: ✅ **Conexión exitosa**
- Si falla: Revisar que PostgreSQL esté corriendo

### 5.5 Guardar la Conexión
- Click en **Finish**
- Se agregará a la lista de conexiones en la izquierda

---

## ✅ PASO 6: Ejecutar el Script `data.sql` en DBeaver

### 6.1 Abrir el Editor SQL
1. En DBeaver, click derecho en la conexión **proyecto_comida**
2. Selecciona **SQL Editor** → **Open SQL Script**

### 6.2 Cargar el archivo data.sql
1. En el editor SQL que se abre: **File** → **Open File**
2. Navega a: `C:\Proyectos\ProyectoComidaDC\src\main\resources\data.sql`
3. Abre el archivo

### 6.3 Ejecutar el Script
1. Selecciona todo el código: `Ctrl + A`
2. Ejecuta: **Ctrl + Enter** o click en el botón ▶️ (Execute)
3. Verás un resultado en la pestaña de abajo que dice:
   ```
   Rows affected: X
   ```

---

## ✅ PASO 7: Verificar los Datos en DBeaver

### 7.1 Ver las Tablas Creadas
En el panel izquierdo:
1. Expande **proyecto_comida**
2. Expande **Schemas** → **public** → **Tables**
3. Deberías ver: USUARIOS, PLATOS, CARRITOS, PEDIDOS, etc.

### 7.2 Ver los Datos Insertados
1. Click derecho en tabla **USUARIOS**
2. Selecciona **View Data**
3. Verás los 4 usuarios que insertamos (Carlos, Ana, Luis, Admin)

---

## ✅ PASO 8: Ejecutar tu Aplicación Spring Boot

### 8.1 Compilar y ejecutar
Abre PowerShell en la carpeta del proyecto:

```powershell
cd C:\Proyectos\ProyectoComidaDC
mvn clean install
mvn spring-boot:run
```

O si tienes Maven en el PATH, puedes hacer click en **Play** en tu IDE.

### 8.2 Verificar que funciona
- Deberías ver en los logs: 
  ```
  Connected to PostgreSQL
  Hibernate tables created
  Data inserted from data.sql
  ```
- Tu aplicación estará en: `http://localhost:3000`
- H2 Console ya NO funcionará (porque cambió a PostgreSQL)

---

## ✅ PASO 9: Consultar los Datos (En DBeaver)

### 9.1 Crear un Query personalizado
1. Click derecho en la conexión
2. **SQL Editor** → **Open SQL Script**
3. Escribe una consulta SQL:

```sql
SELECT * FROM USUARIOS;
SELECT * FROM PLATOS;
SELECT * FROM CARRITOS;
SELECT * FROM PEDIDOS;
```

4. Ejecuta con `Ctrl + Enter`

---

## 🔧 Solucionar Problemas

### Problema 1: "Failed to connect to PostgreSQL"
**Solución:**
```powershell
# Verifica que PostgreSQL esté corriendo
pg_isready
# Debería decir: accepting connections
```

Si no está corriendo:
```powershell
# En Windows, busca "Services" y encuentra "postgresql-x64-XX"
# Dale click derecho → Start
```

### Problema 2: "ERROR: password authentication failed"
**Solución:**
- Verificar que la contraseña en `application.properties` sea `postgres123`
- Si no recuerdas la contraseña, reinstala PostgreSQL

### Problema 3: "Database proyecto_comida does not exist"
**Solución:**
- Vuelve al PASO 2 e intenta crear la BD de nuevo:
```powershell
psql -U postgres
CREATE DATABASE proyecto_comida;
\q
```

### Problema 4: DBeaver no ve las tablas
**Solución:**
1. Cierra la conexión: click derecho → **Disconnect**
2. Abrela de nuevo: click derecho → **Connect**
3. Espera a que se cargue (puede tomar segundos)

---

## 📊 Resumen de Credenciales

| Sistema | Usuario | Contraseña | Puerto |
|---------|---------|-----------|--------|
| **PostgreSQL** | postgres | postgres123 | 5432 |
| **Base de Datos** | - | - | proyecto_comida |
| **App Spring Boot** | - | - | 3000 |

---

## ✨ ¡YA ESTÁ! 

Una vez hayas seguido todos los pasos:
- PostgreSQL está funcionando
- DBeaver está conectado
- Tu BD `proyecto_comida` existe
- Los datos de `data.sql` están en la BD
- Tu aplicación Spring Boot usa PostgreSQL

**Próximos pasos:**
- Ejecuta la aplicación Spring Boot
- Prueba tus endpoints en http://localhost:3000
- Abre DBeaver para ver los cambios en tiempo real

---

**¿Preguntas?** Revisa la sección de "Solucionar Problemas" arriba. 🚀

