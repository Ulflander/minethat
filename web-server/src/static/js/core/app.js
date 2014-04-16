/*jslint browser: true*/
/*global modular, console, $*/


(function (m) {

    "use strict";

    modular.app("Minethat dashboard")

        .consume("users")
        .consume("customers")
        .consume("jobs")
        .consume("documents")
        .consume("concepts")

        .use("auth")
        .use("chat")
        .use("lock")
        
        .route("contact", "contact") // Display contact box
        .route("jobs", "jobs") // Display all jobs
        .route("job/", "job") // Display job
        .route("submit", "submit") // Submit a new job
        .route("documents", "documents") // Display all documents
        .route("search", "search") // Search in documents
        .route("document/", "document") // Display document
        .route("settings", "settings") // User settings

        .start();


}(modular));

