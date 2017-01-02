Engine.define('Timer', function(){
    function Timer(clb, timeout, name) {
        this.clb = clb;
        this.timeout = timeout;
        this.stopTimeout = null;
        this.precision = -1;
        this.name = name;
    }

    Timer.prototype.start = function() {
        var me = this;
        var now = new Date();
        if(me.precision === -1) {
            me.precision = now.getTime();
        }
        me.stopTimeout = setTimeout(function(){
            me.start()
        }, me.precision - now.getTime() + me.timeout);
        me.precision += me.timeout;
        me.clb();
    };

    Timer.prototype.stop = function() {
        console.log((this.name || 'Unnamed timer') + " timeout cleared" );
        clearTimeout(this.stopTimeout);
        this.precision = -1;
    };
    return Timer;
});