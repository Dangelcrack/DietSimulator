# DietSimulator

## 📖 **Introducción**

### **Descripción General**
DietSimulator es una aplicación diseñada para gestionar de manera eficiente dietas personalizadas y su relación con alimentos y usuarios. Está dirigida a entornos como centros de salud, gimnasios y servicios de nutrición personalizada. Su objetivo principal es automatizar las tareas relacionadas con la creación, asignación y administración de dietas, eliminando las ineficiencias del manejo manual.

### **Problema Específico**
Actualmente, la administración de dietas y su asociación con usuarios y comidas suele realizarse manualmente o mediante sistemas dispersos. Esto genera:
- Ineficiencia en la organización.
- Errores humanos al asignar dietas o comidas.
- Dificultades en el seguimiento y actualización de la información.

DietSimulator resuelve estos problemas mediante un sistema centralizado que permite a los usuarios manejar de manera integral:
1. Dietas y su contenido.
2. Alimentos asociados a las dietas.
3. Usuarios con dietas activas.
4. Restricciones funcionales para garantizar integridad y consistencia.

---

## 🎯 **Objetivos del Sistema**

### **Objetivo Principal**
Crear una solución centralizada y automatizada para gestionar dietas, alimentos y usuarios de manera eficiente y sin errores.

### **Objetivos Específicos**
1. **Gestión de Dietas**:
   - Crear, modificar y eliminar dietas personalizadas.
   - Consultar dietas existentes con información detallada.

2. **Asignación Flexible de Comidas**:
   - Relacionar múltiples comidas con una dieta.
   - Garantizar la flexibilidad en la asociación (relación muchos a muchos).

3. **Gestión de Usuarios**:
   - Registrar y gestionar usuarios.
   - Asociar cada usuario con una única dieta activa a la vez.

4. **Eliminación de Entidades**:
   - Gestionar la eliminación de dietas, comidas y usuarios con restricciones funcionales para evitar conflictos.

---

## 🛠️ **Características Principales**

### **Gestión de Dietas**
- Crear nuevas dietas con nombre, descripción y tipo (e.g., vegetariana, keto).
- Editar información de una dieta existente.
- Eliminar dietas mientras se verifica que no estén asignadas a usuarios activos.

### **Relaciones de Dietas y Comidas**
- Asociar comidas específicas a dietas mediante una tabla intermedia.
- Consultar todas las dietas que incluyen una comida específica.
- Buscar dietas por alimentos asociados.

### **Gestión de Usuarios**
- Registrar nuevos usuarios con atributos como nombre, edad, peso, altura, y más.
- Asignar una dieta activa a un usuario.
- Cambiar la dieta activa de un usuario en cualquier momento.

### **Consultas Avanzadas**
- Filtrar dietas según nombre, tipo o comidas específicas.
- Consultar usuarios por dieta activa.
- Realizar búsquedas con operaciones `JOIN` para obtener información relacionada de múltiples tablas.

---

## 🔍 **Estructura del Proyecto**

### **Módulos Principales**
1. **Dietas**:
   - Administración completa de dietas.
   - Funciones de creación, edición y eliminación.

2. **Comidas**:
   - Gestión de alimentos y asociación con dietas.

3. **Usuarios**:
   - Registro, modificación y eliminación de usuarios.
   - Gestión de la dieta activa de cada usuario.

4. **Base de Datos**:
   - Conexión centralizada y consultas SQL optimizadas con soporte para relaciones complejas (e.g., `JOIN`).

5. **Controladores**:
   - Separación clara entre lógica de negocio y presentación.

---

## 📚 **Tecnologías Utilizadas**

- **Lenguaje**: Java
- **Base de Datos**: MySQL
- **ORM**: Implementación manual de consultas SQL.
- **Herramientas de Desarrollo**:
  - IDE: IntelliJ IDEA o Eclipse.
  - Librerías: JDBC para conexión con MySQL.
- **Diseño UML**:
  - Modelo relacional para representar entidades y relaciones.

---

## ⚙️ **Requisitos del Sistema**

### **Requisitos de Software**
- JDK 11 o superior.
- Servidor MySQL.
- Librerías necesarias para la conexión JDBC.

### **Requisitos de Hardware**
- Procesador: Dual-Core o superior.
- Memoria RAM: 4 GB o más.
- Espacio en disco: 500 MB para la base de datos.

---

## 🧩 **Instalación y Configuración**

1. **Clonar el Repositorio**:
   ```bash
   git clone https://github.com/Dangelcrack/DietSimulator/
