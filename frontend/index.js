const express = require("express");

const app = express();

app.use("/", express.static("./static"));

app.listen(process.env.PORT || 8081, () => {
    console.log("Server running");
})