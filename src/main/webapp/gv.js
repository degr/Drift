
setInterval(function() {
    var health = document.getElementById('hk_health').getElementsByClassName('l_val')[0].innerText.split('/').map(v => parseInt(v.trim()));
    health = health[0] / health[1];
    var mana = parseInt(document.getElementsByClassName('gp_val')[0].innerText);
    if(health > 0.7) {
        if(mana > 30) {
            var cp = document.getElementById('cntrl1');
            var links = cp.getElementsByTagName('a');
            links[1].click();
        }
    } else if(health < 0.3) {
        document.getElementById('godvoice').value = 'лечись';
        document.getElementById('voice_submit').click();
    }
}, 30 * 60 * 1000);


var event; // The custom event that will be created

if (document.createEvent) {
    event = document.createEvent("HTMLEvents");
    event.initEvent("dataavailable", true, true);
} else {
    event = document.createEventObject();
    event.eventType = "dataavailable";
}

event.eventName = "dataavailable";

if (document.createEvent) {
    element.dispatchEvent(event);
} else {
    element.fireEvent("on" + event.eventType, event);
}