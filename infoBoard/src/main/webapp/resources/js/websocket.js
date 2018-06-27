
var socket = new WebSocket("ws://localhost:8080/infoBoard/actions");
socket.onmessage = onMessage;

function clearList() {
    $("#resultTable").empty();
    $("#resultOrderTable").empty();
}

function onMessage(event) {
    clearList();

    var jsonParse = JSON.parse(event.data);
    addElements(jsonParse);

}
function addElements(jsonParse) {
    $("#resultTable").append("<tr>" +
        "<td>" + jsonParse.allDrivers +"<td>" + jsonParse.driversOnOrder+
        "</td>" + "<td>" + jsonParse.freeDrivers+
        "</td>" + "<td>" + jsonParse.allTrucks+
        "</td>" + "<td>" + jsonParse.trucksOnOrder+
        "</td>" + "<td>" + jsonParse.availableTrucks+
        "</td>" + "<td>" + jsonParse.brokenTrucks +
        "</td>" +
        "</tr>");

    var listOrders = jsonParse.lastTenOrders;

    for (var i=0; i<listOrders.length; i++){

        $("#resultOrderTable").append("<tr>" +
            "<td>" + listOrders[i].id +"<td>" + listOrders[i].orderStatus+
            "</td>" + "<td>" + listOrders[i].truck+
            "</td>" + "<td>" + listOrders[i].cargoes+
            "</td>" + "<td>" + listOrders[i].drivers+
            "</td>" +
            "</tr>");
    }

}


