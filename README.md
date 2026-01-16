# GeoNotes 📍📝

GeoNotes es una aplicacion Android desarrollada como feedback de la asignatura **Programacion Dirigida por Eventos**.

La app permite crear notas asociadas a una ubicacion en un mapa, visualizarlas en una lista, y consultarlas rapidamente desde un **widget de escritorio**.  
Incluye tambien un ejemplo de uso de **sensores (acelerometro)** junto con una **vista personalizada usando Canvas**.

---

## ✨ funcionalidades principales

- 📍 seleccion de ubicaciones mediante **long press en Google Maps**
- 📝 creacion de notas geolocalizadas
- 📋 listado de notas guardadas
- 👉 pulsar una nota centra el mapa en su ubicacion
- 🧠 persistencia simple usando `SharedPreferences`
- 🧩 widget de escritorio:
    - muestra el numero total de notas
    - muestra el texto de la ultima nota guardada
- 📱 uso del acelerometro con:
    - vista personalizada
    - dibujo dinamico usando `Canvas`
- 🔄 comunicacion entre fragments mediante listeners

---

## 🧱 estructura general

- `MainActivity`
    - coordina el mapa y el panel de notas
- `MapFragment`
    - gestion del mapa y seleccion de ubicaciones
- `NoteFragment`
    - creacion y listado de notas
- `NotesListFragment`
    - recycler con las notas guardadas
- `NoteRepository`
    - persistencia de notas en formato JSON
- `SensorFragment` + `CircleView`
    - uso del acelerometro y dibujo en canvas
- `NoteCountWidgetProvider`
    - widget con contador y ultima nota

---

## 🔑 configuracion de Google Maps (MUY IMPORTANTE)

Por **motivos de seguridad**, la **API Key de Google Maps NO se incluye en el repositorio**.

Esto es **intencionado** y sigue exactamente las indicaciones del profesor.

### 📌 donde va la API key

La clave se define en el siguiente fichero:

app/src/main/res/values/google_maps_api.xml



<resources>
    <string name="google_maps_key"></string>
</resources>

En el repositorio, este fichero contiene la clave **vacia**:


▶️ como ejecutar el proyecto

Para probar la aplicacion con tu propia API key:

Abre el fichero:


app/src/main/res/values/google_maps_api.xml
Introduce tu clave entre las etiquetas:


<string name="google_maps_key">TU_API_KEY_AQUI</string>
Ejecuta el proyecto normalmente en el emulador o dispositivo.

⚠️ Nota importante
La API key no debe subirse al repositorio.
El proyecto esta preparado para que cada persona use la suya propia.

📦 sobre la APK


✅ La APK final SI debe compilarse con una API key valida

🔒 Esa clave debe estar restringida por nombre de paquete y SHA-1

📂 El codigo fuente entregado debe tener la clave vacia



📐 permisos utilizados
En el AndroidManifest.xml se declaran:

permisos de ubicacion (ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

permisos de red (INTERNET, ACCESS_NETWORK_STATE)

Estos ultimos se incluyen para asegurar que Google Maps funcione correctamente
independientemente del emulador o dispositivo utilizado para la correccion.

🧪 widget de escritorio
El proyecto incluye un widget que:

muestra el numero total de notas

muestra el texto de la ultima nota guardada

abre la app al pulsarlo

El widget no se actualiza automaticamente por tiempo, sino solo cuando cambian los datos,
para evitar consumo innecesario de recursos.

👤 autor

Proyecto desarrollado por Juan Manuel Torrado