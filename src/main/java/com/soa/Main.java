package com.soa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.graphQL.GraphQLProvider;
import spark.Spark;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        GraphQLProvider provider = new GraphQLProvider();
        ObjectMapper mapper = new ObjectMapper();

        Spark.port(7070);
        Spark.post("/graphql", (req, res) -> {
            Map<String, Object> payload = mapper.readValue(req.body(), Map.class);
            String query = (String) payload.get("query");

            Map<String, Object> result = provider.getGraphQL()
                    .execute(query)
                    .toSpecification();

            res.type("application/json");
            return mapper.writeValueAsString(result);
        });
    }
}