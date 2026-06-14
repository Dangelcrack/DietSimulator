# 🥗 DietSimulator

Aplicación de escritorio para la **gestión centralizada de dietas personalizadas**, desarrollada en Java con JavaFX. Permite administrar usuarios, dietas y alimentos desde una interfaz gráfica, con soporte para múltiples motores de base de datos.

---

## ✨ Características principales

- 👤 **Gestión de usuarios** — registro, edición y asignación de dieta activa
- 🥦 **Gestión de alimentos** — catálogo completo con asociación a dietas
- 📋 **Gestión de dietas** — creación, edición y eliminación con validación de integridad
- 🔗 **Relación muchos a muchos** entre dietas y comidas
- 🔍 **Consultas avanzadas** con filtros por nombre, tipo o alimento
- 🗄️ **Multi-base de datos** — compatible con MySQL, MariaDB y H2

---

## 🛠️ Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| JavaFX | Interfaz gráfica (FXML) |
| JDBC | Conexión con base de datos |
| MySQL / MariaDB / H2 | Motores de base de datos |
| Maven | Gestión de dependencias |
| IntelliJ IDEA | IDE de desarrollo |

---

## ▶️ Instalación y ejecución

### Requisitos previos
- Java 21 o superior
- Maven 3.x
- MySQL, MariaDB o H2 (según la configuración elegida)

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/Dangelcrack/DietSimulator.git
cd DietSimulator

# 2. Configurar la conexión a la base de datos
# Edita el archivo connectionmysql.xml / connectionmariadb.xml / connectionh2.xml
# según el motor que vayas a usar

# 3. Compilar y ejecutar
mvn javafx:run
```

### Configuración de base de datos

El proyecto incluye tres archivos de configuración listos para usar:

- `connectionmysql.xml` → MySQL
- `connectionmariadb.xml` → MariaDB
- `connectionh2.xml` → H2 (embebido, sin instalación)

---

## 📁 Estructura del proyecto

```
DietSimulator/
├── src/main/
│   ├── java/              # Lógica de negocio y controladores
│   └── resources/         # FXML, CSS y recursos visuales
├── connectionmysql.xml    # Config MySQL
├── connectionmariadb.xml  # Config MariaDB
├── connectionh2.xml       # Config H2
├── pom.xml                # Dependencias Maven
└── README.md
```

---

## 🗂️ Módulos del sistema

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Usuarios  │────▶│    Dietas   │◀────│   Comidas   │
│             │     │             │     │             │
│ nombre      │     │ nombre      │     │ nombre      │
│ edad        │     │ descripción │     │ calorías    │
│ peso        │     │ tipo        │     │ tipo        │
│ altura      │     │             │     │             │
└─────────────┘     └─────────────┘     └─────────────┘
  1 dieta activa     muchos usuarios      muchas dietas
```

---

## 👤 Autor

**Ángel Guerrero** — [@Dangelcrack](https://github.com/Dangelcrack)

📧 angelguerrero540@gmail.com

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.
