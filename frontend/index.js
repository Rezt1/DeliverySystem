const express = require("express");

const app = express();

app.use("/", express.static("./static"));

app.listen(8082, () => {
    console.log("Server running on PORT 8082");
})