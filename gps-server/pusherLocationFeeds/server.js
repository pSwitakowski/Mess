var express = require("express")
var Pusher = require("pusher")
var bodyParser = require("body-parser")

const pusher = new Pusher({
    appId: "1223490",
    key: "6389e8eb55333cc7c145",
    secret: "4f7f331f7eb612541af7",
    cluster: "eu",
    useTLS: true
});


var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));


app.post('/location', (req, res,next)=>{
        
    var longitude = req.body.longitude;
    var latitude = req.body.latitude;
    var username = req.body.username;

    pusher.trigger('feed', 'location', {longitude, latitude, username});
    res.json({success: 200});
});


app.listen(4040, function () {
    console.log('Listening on 4040')
})