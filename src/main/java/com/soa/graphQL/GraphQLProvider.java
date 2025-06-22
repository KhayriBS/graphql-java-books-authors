package com.soa.graphQL;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.soa.data.DataStore;
import com.soa.entity.Author;
import com.soa.entity.Book;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;



import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//Classe responsable de la configuration et de la création de l'instance GraphQL.
//Elle charge le schéma GraphQL à partir d'un fichier, définit les résolveurs (data fetchers),
// et crée un objet GraphQL prêt à être utilisé pour exécuter des requêtes.
public class GraphQLProvider {
    //Instance principale de GraphQL qui sera utilisée pour exécuter les requêtes
    private final GraphQL graphQL;

    //Constructeur qui initialise l'objet GraphQL.
    //Il lit le schéma depuis un fichier .graphqls, construit le wiring (liens entre schéma et données),
    ///puis génère le schéma exécutable.

    public GraphQLProvider() {
        // Chargement du fichier de schéma GraphQL (ici shema.graphqls dans les ressources)
        TypeDefinitionRegistry typeRegistry = new SchemaParser()
                .parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("schemas.graphqls")));
        // Construction des résolveurs (liens entre les types GraphQL et les sources de données)
        RuntimeWiring wiring = buildWiring();

        //Génération du schéma exécutable GraphQL à partir du schéma statique + wiring
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
        // Création de l'objet GraphQL prêt à exécuter des requêtes
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }


    //Méthode privée qui définit les resolvers (data fetchers) pour chaque type et champ du schéma GraphQL.
    //Elle mappe les types et champs du schéma à des fonctions Java qui retournent les données.

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        // Résolveur pour retourner tous les livres
                        .dataFetcher("books", env -> DataStore.books)
                        // Résolveur pour retourner tous les auteurs
                        .dataFetcher("authors", env -> DataStore.authors)
                        // Résolveur pour retrouver un livre par son identifiant
                        .dataFetcher("bookById", env -> {
                            String id = env.getArgument("id");
                            return DataStore.books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
                        })
                        .dataFetcher("authorById", env -> {
                            String id = env.getArgument("id");
                            return DataStore.authors.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
                        })
                )
                .type("Book", typeWiring -> typeWiring
                        // Résolveur pour retourner l'auteur d'un livre
                        .dataFetcher("author", env -> {
                            Book book = env.getSource();
                            return DataStore.authors.stream()
                                    .filter(a -> a.getId().equals(book.getAuthorId()))
                                    .findFirst().orElse(null);
                        })
                )
                .type("Author", typeWiring -> typeWiring
                        // Résolveur pour retourner tous les livres écrits par un auteur
                        .dataFetcher("books", env -> {
                            Author author = env.getSource();
                            return DataStore.books.stream()
                                    .filter(b -> b.getAuthorId().equals(author.getId()))
                                    .collect(Collectors.toList());
                        })
                ).type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("addAuthor", env -> {
                            String id = env.getArgument("id");
                            String name = env.getArgument("name");

                            if (id == null || name == null) {
                                throw new RuntimeException("Arguments 'id' or 'name' are null.");
                            }

                            if (DataStore.authors == null) {
                                throw new RuntimeException("DataStore.authors is null.");
                            }

                            Author author = new Author(id, name);
                            DataStore.authors.add(author);
                            return author;
                        })

                        .dataFetcher("addBook", env -> {
                            String id = env.getArgument("id");
                            String title = env.getArgument("title");
                            String authorId = env.getArgument("authorId");

                            // Vérifie si l'auteur existe
                            Author author = DataStore.authors.stream()
                                    .filter(a -> a.getId().equals(authorId))
                                    .findFirst()
                                    .orElse(null);

                            if (author == null) {
                                throw new RuntimeException("Author with ID " + authorId + " not found.");
                            }

                            Book book = new Book(id, title, authorId);
                            DataStore.books.add(book);
                            return book;
                        })


                )
                .build();
    }
    //    Getter pour accéder à l'instance GraphQL.
    public GraphQL getGraphQL() {

        return graphQL;
    }
}