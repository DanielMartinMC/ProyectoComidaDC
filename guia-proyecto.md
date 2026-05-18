# Guía de Inicio y Despliegue del Proyecto

---

## Backend — IntelliJ IDEA

1. Abrir IntelliJ → `File → Open` → seleccionar la carpeta raíz del proyecto.
2. Esperar a que descargue las dependencias de Maven/Gradle (barra de progreso inferior).
3. Revisar `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/mi_bd
   spring.datasource.username=postgres
   spring.datasource.password=tu_password
   server.port=8080
   ```
4. Buscar la clase con `@SpringBootApplication` y pulsar el botón ▶, o:
   - **Windows/Linux:** `Shift + F10`
   - **macOS:** `Control + R`
5. Cuando en la consola aparezca `Started Application in X seconds`, el backend está en `http://localhost:8080`.

---

## Frontend — Angular

```bash
npm install        # solo la primera vez o si cambia package.json
ng serve -o        # compila, levanta el servidor y abre el navegador
```

El flag `-o` abre automáticamente `http://localhost:4200`. Cualquier cambio en el código recarga el navegador solo.

Revisar que `src/environments/environment.ts` apunte al backend correcto:
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

---

## Despliegue Backend — Render

1. Ir a [render.com](https://render.com) → `New → Web Service`.
2. Conectar el repositorio de GitHub.
3. Configurar:

   | Campo | Valor |
   |---|---|
   | Environment | `Java` |
   | Build Command | `./mvnw clean package -DskipTests` |
   | Start Command | `java -jar target/*.jar` |
   | Branch | `main` |

4. En `Environment Variables` añadir las variables de la BD y las que necesite la app.
5. Guardar → Render desplegará automáticamente en cada push.

> La URL quedará como `https://mi-proyecto.onrender.com`

---

## Despliegue Frontend — Vercel

1. Actualizar `src/environments/environment.prod.ts` con la URL del backend en Render:
   ```typescript
   export const environment = {
     production: true,
     apiUrl: 'https://mi-proyecto.onrender.com/api'
   };
   ```

2. Crear `vercel.json` en la raíz del proyecto (necesario para que las rutas de Angular no den 404):
   ```json
   {
     "rewrites": [
       { "source": "/(.*)", "destination": "/index.html" }
     ]
   }
   ```

3. Ir a [vercel.com](https://vercel.com) → `Add New Project` → importar el repositorio.
4. Verificar la configuración de build:

   | Campo | Valor |
   |---|---|
   | Framework Preset | `Angular` |
   | Build Command | `ng build --configuration production` |
   | Output Directory | `dist/nombre-del-proyecto/browser` |

5. Deploy → Vercel desplegará automáticamente en cada push.

> La URL quedará como `https://mi-proyecto.vercel.app`

---

## Flujo completo

```
Local
├── Backend:   IntelliJ ▶  →  http://localhost:8080
└── Frontend:  ng serve -o  →  http://localhost:4200

Producción
├── Backend:   push → Render  →  https://mi-proyecto.onrender.com
└── Frontend:  push → Vercel  →  https://mi-proyecto.vercel.app
```
