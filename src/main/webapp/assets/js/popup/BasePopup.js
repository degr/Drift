Engine.define("BasePopup", ['Popup', 'Dom'], function () {
    var Popup = Engine.require('Popup');
    var Dom = Engine.require('Dom');

    function BasePopup(baseName) {
        this.popup = new Popup({
            title: 'Base ' + baseName,
            isOpen: true,
            minimized: false,
            controlMinimize: false,
            withOverlay: true,
            content: this.getMainPanel()
        });
    }

    BasePopup.prototype.getMainPanel = function () {
        var me = this;
        var dock = this.generateButton("Dock", function(){
            me.popup.setContent(me.getDockPanel())
        });
        var market = this.generateButton("Market", function(){
            me.popup.setContent(me.getMarketPanel())
        });
        var computer = this.generateButton("Computer", function(){
            me.popup.setContent(me.getComputerPanel())
        });
        var launch = this.generateButton("Launch", function(){
            me.popup.setContent(me.getMarketPanel())
        });

        return Dom.el('div', null, [
            dock, market, computer
        ])
    }

    BasePopup.prototype.generateButton = function(title, callback) {
        return Dom.el('a', {href: '#', class: 'button', onclick: function(e){
            e.preventDefault();
            callback();
        }});
    }
});