# 📘 GraphQL Java - Books & Authors (Sans Base de Données)

Ce projet démontre comment créer une API **GraphQL** simple en **Java**, sans utiliser de base de données. Les données sont stockées en mémoire (via des listes statiques).

---

## 🚀 Technologies utilisées

- Java 17
- Spring Boot
- GraphQL Java Tools
- Maven

---

## 📚 Fonctionnalités

- Requête `allBooks` : liste tous les livres
- Requête `bookById(id)` : récupère un livre par ID
- Requête `allAuthors` : liste tous les auteurs
- Requête `authorById(id)` : récupère un auteur par ID

---

## 🧩 Structure du projet
````
tp_GraphQL/
├── .idea/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── soa/
│ │ │ ├── data/ # Données simulées (ex: listes statiques)
│ │ │ ├── entity/ # Entités du modèle (Book, Author)
│ │ │ └── graphQL/ # Resolvers, configuration GraphQL
│ │ │ └── Main.java # Point d’entrée de l'application
│ │ └── resources/
│ └── test/
├── target/
├── .gitignore
├── pom.xml
````


---

## 🧪 Exemples de requêtes

### Obtenir tous les livres

```graphql
query {
  allBooks {
    id
    title
    author {
      id
      name
    }
  }
}
````

### 💡 À propos
Ce projet est un bon point de départ pour comprendre les bases de GraphQL avec Java, sans les complexités d’une base de données.
