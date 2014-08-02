$(function() {
    var Bird = Backbone.Model.extend({
        defaults: {
            name : '',
            wingspan : 15,
            weight : 10
        },
        initialize: function() {
            console.log('unitialized');
            this.on("change:weight", function() {
                console.log("weight changed:"+this.get('weight'));
            });
        },

    });

    var Tree = Backbone.Model.extend({
            defaults: {
                name : '',
                height : 15,
                weight : 10
            },
            initialize: function() {
                this.on("change:weight", function() {
                    console.log("weight changed:"+this.get('weight'));
                });
            },

     });

//    var BirdList = Backbone.Collection.extend({
//        model : Bird,
//        localStorage : new Backbone.LocalStorage('birds')
//   });

    var BirdView = Backbone.View.extend({
        el : $('#birds'),
        render : function(event) {
            var compiled_template = _.template( $('#bird-template').html() );
            this.$el.html(compiled_template(this.model.toJSON()));
            return this;
        },
        initialize : function() {

        }
    });

    var TreeView = Backbone.View.extend({
        el : $('#trees'),
        render : function(event) {
            var compiled_template = _.template( $('#tree-template').html() );
            this.$el.html(compiled_template(this.model.toJSON()));
            return this;
        },
        initialize : function() {

        }
    });

    var AppRouter = Backbone.Router.extend({
        routes : {
            "birds" : "showBirds",
            "trees" : "showTrees",
            "*other" : "defaultRoute"
        },
        showBirds : function() {
            console.log('show birds');
            var view = new BirdView({model : new Bird( {name : 'Owl'} )});
            view.render();
        },
        showTrees : function() {
            var view = new TreeView({model : new Tree( {name : 'pine'} )});
            view.render();
        },
        defaultRoute : function() {
            console.log('default');
        }
    });

    var routerInstance = new AppRouter();

    Backbone.history.start();
    //routerInstance.navigate();

});
