1 create project using curl https://start.spring.io/starter.tgz \
                                                                                             -d dependencies=webflux,data-neo4j \
                                                                                             -d type=gradle-project \
                                                                                             -d bootVersion=3.4.5 \
                                                                                             -d baseDir=Neo4jSpringBootExampleGradle \
                                                                                             -d name=Neo4j%20SpringBoot%20Example | tar -xzvf
2 Create project based on https://neo4j.com/docs/getting-started/languages-guides/java/spring-data-neo4j/

3 run neo4j docker
docker run \
    --restart always \
    --publish=7474:7474 --publish=7687:7687 \
    --env NEO4J_AUTH=REPLACE_WITH_USERNAME/REPLACE_WITH_PASWD \
    --volume=<replace-with-path-to-some-folder-other>/neo4jdb:/data \
    neo4j:2025.10.1

4 Open the browser http://localhost:7474/browser/ and login

5 `:play movies` in the command create the db by clicking the editor and press play

