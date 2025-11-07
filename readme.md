# Tasks API - Arquitectura Hexagonal

API REST para gestión de tareas implementada con arquitectura hexagonal (puertos y adaptadores).

## Descripción

Este proyecto implementa un CRUD de tareas siguiendo los principios de arquitectura hexagonal, donde el dominio es independiente de frameworks y tecnologías externas.

> **Nota:** Este proyecto fue desarrollado como práctica para una entrevista técnica, siguiendo el tutorial de [YouTube](https://www.youtube.com/watch?v=JD_ZL3Bnaog) sobre arquitectura hexagonal con Spring Boot.

## Arquitectura Hexagonal

### Principios básicos:
- **Dominio en el centro**: La lógica de negocio no depende de nada externo
- **Puertos**: Interfaces que definen contratos
- **Adaptadores**: Implementaciones concretas de los puertos
- **Inversión de dependencias**: Las capas externas dependen del dominio, no al revés

### Estructura del proyecto:

```
src/main/java/com/hexagonal/tasks/
├── application/                    # LÓGICA DE APLICACIÓN
│   ├── services/                  # Fachada que coordina casos de uso
│   │   └── TaskService.java
│   └── usecases/                  # Casos de uso específicos (un UseCase = una operación)
│       ├── CreateTaskUseCaseImpl
│       ├── DeleteTaskUseCaseImpl
│       ├── GetAdditionalTaskInfoUseCaseImpl
│       ├── RetrieveTaskUseCaseImpl
│       └── UpdateTaskUseCaseImpl
│
├── domain/                         # NÚCLEO - Sin dependencias externas
│   ├── models/                    # Entidades de negocio (POJOs puros)
│   │   ├── AdditionalTaskInfo
│   │   └── Task
│   └── ports/                     # Interfaces (contratos)
│       ├── in/                    # Puertos de entrada (casos de uso)
│       │   ├── CreateTaskUseCase
│       │   ├── DeleteTaskUseCase
│       │   ├── GetAdditionalTaskInfoUseCase
│       │   ├── RetrieveTaskUseCase
│       │   └── UpdateTaskUseCase
│       └── out/                   # Puertos de salida (repositorios)
│           ├── ExternalServicePort
│           └── TaskRepositoryPort
│
├── infrastructure/                 # IMPLEMENTACIONES TÉCNICAS
│   ├── adapters/                  # Adaptadores externos
│   │   └── ExternalServiceAdapter
│   ├── config/                    # Configuración Spring
│   │   └── ApplicationConfig
│   ├── controllers/               # REST Controllers (entrada)
│   │   └── TaskController
│   ├── entities/                  # Entidades JPA
│   │   └── TaskEntity
│   ├── mappers/                   # Conversión Domain ↔ Entity
│   │   └── TaskMapper
│   └── repositories/              # Repositorios JPA (salida)
│       ├── JpaTaskRepository
│       └── JpaTaskRepositoryAdapter
│
└── TasksApplication.java           # Main de Spring Boot
```

## Flujo de una petición

```
1. HTTP Request
   ↓
2. TaskController (infrastructure/controllers) - Recibe petición REST
   ↓
3. TaskService (application/services) - Coordina la operación
   ↓
4. UseCase Implementation (application/usecases) - Ejecuta caso de uso específico
   ↓
5. Port Interface (domain/ports/out) - Interface del repositorio
   ↓
6. JpaTaskRepositoryAdapter (infrastructure/repositories) - Implementación JPA
   ↓
7. JpaTaskRepository (infrastructure/repositories) - Spring Data JPA
   ↓
8. Database (MySQL) - TaskEntity
```

## Capas detalladas

### 1. Domain (Dominio)
**Propósito:** Lógica de negocio pura, sin dependencias externas

**Contiene:**
- `models/Task.java` - Entidad de dominio (POJO simple)
- `models/AdditionalTaskInfo.java` - Información adicional de tarea
- `ports/in/` - Casos de uso (interfaces):
    - `CreateTaskUseCase` - Crear tarea
    - `DeleteTaskUseCase` - Eliminar tarea
    - `GetAdditionalTaskInfoUseCase` - Obtener info adicional
    - `RetrieveTaskUseCase` - Obtener/Listar tareas
    - `UpdateTaskUseCase` - Actualizar tarea
- `ports/out/` - Contratos externos:
    - `TaskRepositoryPort` - Contrato del repositorio
    - `ExternalServicePort` - Contrato de servicios externos

**Reglas:**
- ❌ No usar anotaciones de Spring (@Service, @Component)
- ❌ No usar anotaciones de JPA (@Entity, @Table)
- ✅ Solo Java puro y lógica de negocio
- ✅ Las interfaces definen QUÉ se hace, no CÓMO

### 2. Application (Aplicación)
**Propósito:** Implementar los casos de uso del dominio

**Contiene:**
- `services/TaskService.java` - Fachada que coordina los casos de uso
- `usecases/` - Implementaciones específicas de cada caso de uso:
    - `CreateTaskUseCaseImpl`
    - `DeleteTaskUseCaseImpl`
    - `GetAdditionalTaskInfoUseCaseImpl`
    - `RetrieveTaskUseCaseImpl`
    - `UpdateTaskUseCaseImpl`

**Reglas:**
- ✅ Usa @Service de Spring
- ✅ Implementa las interfaces del domain/ports/in
- ✅ Depende de domain/ports/out (no de implementaciones concretas)
- ✅ Coordina la lógica de negocio

### 3. Infrastructure (Infraestructura)
**Propósito:** Detalles técnicos y frameworks

**Contiene:**

**Controllers (entrada HTTP):**
- `controllers/TaskController.java` - REST API (@RestController)

**Repositories (salida BD):**
- `repositories/JpaTaskRepository.java` - Interface Spring Data JPA
- `repositories/JpaTaskRepositoryAdapter.java` - Adaptador que implementa `TaskRepositoryPort`

**Entities (persistencia):**
- `entities/TaskEntity.java` - Entidad JPA (@Entity) para la BD

**Mappers (conversión):**
- `mappers/TaskMapper.java` - Convierte Task ↔ TaskEntity (usando MapStruct en rama feature)

**Adapters (servicios externos):**
- `adapters/ExternalServiceAdapter.java` - Implementa `ExternalServicePort`

**Config (configuración):**
- `config/ApplicationConfig.java` - Beans de Spring (@Configuration)

**Reglas:**
- ✅ Usa anotaciones de Spring y JPA
- ✅ Implementa los puertos del dominio
- ✅ Maneja detalles técnicos (BD, HTTP, etc.)
- ✅ Depende del dominio, nunca al revés

## Mapeo de conceptos

| Concepto Hexagonal | En este proyecto |
|-------------------|------------------|
| Puerto de entrada | `CreateTaskUseCase`, `RetrieveTaskUseCase`, etc. (interfaces en domain/ports/in) |
| Puerto de salida | `TaskRepositoryPort`, `ExternalServicePort` (interfaces en domain/ports/out) |
| Adaptador de entrada | `TaskController` (infrastructure/controllers) |
| Adaptador de salida | `JpaTaskRepositoryAdapter` (infrastructure/repositories) |
| Dominio | `Task`, `AdditionalTaskInfo` (domain/models) |
| Casos de uso | `CreateTaskUseCaseImpl`, `DeleteTaskUseCaseImpl`, etc. (application/usecases) |

## Endpoints disponibles

### Tasks CRUD
```
POST   /api/tasks/create                  - Crear tarea
GET    /api/tasks                         - Listar todas las tareas
GET    /api/tasks/{taskId}                - Obtener tarea por ID
PUT    /api/tasks/{taskId}                - Actualizar tarea
DELETE /api/tasks/{taskId}                - Eliminar tarea
GET    /api/tasks/{taskId}/additionalInfo - Info adicional de tarea
```

## Tecnologías

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- MySQL
- Maven
- Lombok (en rama feature)
- MapStruct (en rama feature)

## Configuración

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tasks
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
```

### Base de datos
```sql
CREATE DATABASE tasks;
```

## Ejecución

1. Crear la base de datos MySQL
2. Configurar credenciales en `application.properties`
3. Ejecutar: `mvn spring-boot:run`
4. API disponible en: `http://localhost:8080`

## Ejemplo de uso (Postman)

### Crear tarea
```json
POST http://localhost:8080/api/tasks/create

{
  "title": "Completar proyecto",
  "description": "Implementar arquitectura hexagonal",
  "completed": false
}
```

La colección completa de Postman está disponible en: `docs/postman/tasks-api.postman_collection.json`

## Ventajas de esta arquitectura

1. **Testabilidad:** Fácil hacer tests unitarios del dominio
2. **Independencia:** El dominio no depende de frameworks
3. **Flexibilidad:** Fácil cambiar BD o framework sin tocar el dominio
4. **Mantenibilidad:** Separación clara de responsabilidades
5. **Escalabilidad:** Fácil agregar nuevos adaptadores (GraphQL, gRPC, etc.)

## Conceptos clave para recordar

### ¿Por qué dos modelos (Task y TaskEntity)?
- **Task (domain/models):** Lógica de negocio pura, sin anotaciones JPA
- **TaskEntity (infrastructure/entities):** Representación en BD con anotaciones JPA (@Entity, @Table, etc.)
- **TaskMapper (infrastructure/mappers):** Convierte entre Task ↔ TaskEntity

### ¿Por qué interfaces (ports)?
- Definen contratos sin implementación
- Permiten inversión de dependencias (el dominio no depende de infraestructura)
- Facilitan testing con mocks
- Separan QUÉ se hace (puerto) de CÓMO se hace (adaptador)

### ¿Por qué un UseCase por cada operación?
- **Single Responsibility Principle (SRP):** Cada clase tiene una sola responsabilidad
- Más fácil de testear independientemente
- Más fácil de mantener y modificar
- Claridad: sabes exactamente qué hace cada clase

### ¿Cuál es el rol del TaskService?
- Actúa como **fachada** que expone todos los casos de uso
- Simplifica el Controller (un solo punto de entrada)
- Útil para coordinar múltiples UseCases en una operación compleja
- En arquitectura hexagonal pura es opcional (puedes inyectar UseCases directamente en el Controller)

### ¿Dónde va cada cosa?
- **Reglas de negocio** → Domain (models)
- **Contratos/Interfaces** → Domain (ports)
- **Casos de uso específicos** → Application (usecases)
- **Coordinación de casos de uso** → Application (services)
- **REST/HTTP** → Infrastructure (controllers)
- **Base de datos (JPA)** → Infrastructure (repositories, entities)
- **Conversión de datos** → Infrastructure (mappers)
- **Configuración Spring** → Infrastructure (config)

## Patrón de nombrado

**Domain (Puertos):**
- Interfaces de caso de uso (in): `*UseCase` (ej: `CreateTaskUseCase`)
- Interfaces de repositorio (out): `*Port` (ej: `TaskRepositoryPort`)

**Application:**
- Implementación de casos de uso: `*UseCaseImpl` (ej: `CreateTaskUseCaseImpl`)
- Servicio orquestador: `*Service` (ej: `TaskService`)

**Infrastructure:**
- Controlador REST: `*Controller` (ej: `TaskController`)
- Repositorio JPA: `Jpa*Repository` (ej: `JpaTaskRepository`)
- Adaptador de repositorio: `Jpa*RepositoryAdapter` (ej: `JpaTaskRepositoryAdapter`)
- Entidad JPA: `*Entity` (ej: `TaskEntity`)
- Mapper: `*Mapper` (ej: `TaskMapper`)
- Adaptador de servicio externo: `*ServiceAdapter` (ej: `ExternalServiceAdapter`)

**Domain (Modelos):**
- Entidad de dominio: sin sufijo (ej: `Task`, `AdditionalTaskInfo`)

## Estructura de Ramas

Este proyecto utiliza Git Flow para organizar diferentes versiones del código:

### `main`
Código base con implementación limpia usando constructores tradicionales de Java.
- Constructores manuales explícitos
- Sin Lombok
- Mappers manuales
- **Ideal para:** Entender la estructura base sin abstracciones adicionales

### `feature/add-lombok-and-mapper`
Refactorización del código aplicando mejores prácticas:
- **Lombok**: Reduce boilerplate con `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`
- **MapStruct**: Generación automática de mappers para Task ↔ TaskEntity
- Código más limpio y mantenible
- **Ideal para:** Producción y proyectos reales

### `docs/add-readme`
Rama dedicada para la documentación del proyecto (este README).

---

### Recomendación de uso

| Propósito | Rama recomendada |
|-----------|------------------|
| Aprendizaje inicial | `main` |
| Entender arquitectura hexagonal | `main` |
| Ver código de producción | `feature/add-lombok-and-mapper` |
| Implementar en proyecto real | `feature/add-lombok-and-mapper` |
| Consultar documentación | `docs/add-readme` (o cualquiera después del merge) |

---

## Referencias

- [Arquitectura Hexagonal - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Tutorial YouTube - Arquitectura Hexagonal con Spring Boot](https://www.youtube.com/watch?v=JD_ZL3Bnaog)
- [DDD y Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

##  Autor

Proyecto de práctica para entrevista técnica - Arquitectura Hexagonal con Spring Boot

---

## Notas para la entrevista

Este proyecto fue desarrollado como preparación para una prueba técnica presencial. La estructura sigue los principios de:
- **Arquitectura Hexagonal** (Ports & Adapters)
- **Clean Architecture** (Inversión de dependencias)
- **SOLID Principles** (especialmente SRP y DIP)
- **Domain-Driven Design** (DDD) basics

El objetivo es demostrar comprensión de arquitecturas limpias, separación de responsabilidades y buenas prácticas de desarrollo.