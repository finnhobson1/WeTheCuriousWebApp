if (JSON && JSON.stringify && JSON.parse) var Session = Session || (function() {
    // window object
    var win = window.top || window;

    // session store
    var store = (win.name ? JSON.parse(win.name) : {});

    // save store on page unload
    function Save() {
        win.name = JSON.stringify(store);
    };

    // page unload event
    if (window.addEventListener) window.addEventListener("unload", Save, false);
    else if (window.attachEvent) window.attachEvent("onunload", Save);
    else window.onunload = Save;

    // public methods
    return {
        // set a session variable
        set: function(name, value) {
            store[name] = value;
        },

        // get a session value
        get: function(name) {
            return (store[name] ? store[name] : undefined);
        },

        // clear session
        clear: function() { store = {}; },

        // dump session data
        dump: function() { return JSON.stringify(store); }
    };
})();

// <![CDATA[

// initialize application defaults
var counter = Session.get("counter") || {
    visits: 0,
    time: []
};

// onload
window.onload = function() {
    // var d = new Date();
    counter.visits++;
    // counter.time.push(Pad(d.getHours()) + ":" + Pad(d.getMinutes()) + ":" + Pad(d.getSeconds()));
    // if (counter.time.length 10) counter.time = counter.time.slice(1);

    var modal = document.querySelector('.modal');
    if (counter.visits < 2) modal.classList.add('is-active');

    // store value in session
    Session.set("counter", counter);
};

// add leading zeros
function Pad(n) {
    n = "00" + n;
    return n.substr(n.length-2);
}
// ]]>
