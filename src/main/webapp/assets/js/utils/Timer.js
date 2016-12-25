Engine.define('Timer', function(){
    function Timer(clb, timeout) {
        this.clb = clb;
        this.timeout = timeout;
        this.stopTimeout = null;
        this.precision = -1;
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
        clearTimeout(this.stopTimeout);
        this.precision = -1;
    };
    return Timer;
});