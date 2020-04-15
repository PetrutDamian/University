function toList2(source){
    let destination = document.getElementById("2");
    move(source, destination);
}
function toList1(source){
    let destination = document.getElementById("1");
    move(source, destination);
}
function move(source, destination){
    let index = destination.selectedIndex;
    destination.appendChild(source[source.selectedIndex]);
    destination.selectedIndex = index;
}