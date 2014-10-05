'use strict';



var app = angular.module(
	'leavesystem',
	[
		'leavesystem.directives', 
		'leavesystem.services'
	]
);
var currentUserG = {
	"id":getCookie("u_id"),
	"name":getCookie("u_name"),
	"position":getCookie("u_position"),
	"department_id":getCookie("u_department")
};

function defaultCase(name){
	var df = "";
	switch(name){
		case "u_name":
			df = "not login"; break;
		default:
			df = "";
	}
	return df;
}
function SetCookie(name,value)//两个参数，一个是cookie的名子，一个是值
{
    //var Days = 30; //此 cookie 将被保存 30 天
    //var exp  = new Date();    //new Date("December 31, 9998");
    //exp.setTime(exp.getTime() + Days*24*60*60*1000);
    document.cookie = name + "="+ escape (value) + ";"/*expires= + exp.toGMTString()*/;
}
function getCookie(name)//取cookies函数        
{
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
     if(arr != null) return unescape(arr[2]); return defaultCase(name);

}
function delCookie(name)//删除cookie
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
		
app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
		.when(
			'/', 
			{
				redirectTo:'/login'
			}
		)
		.when(
			'/login',
			{
				controller:'LoginCtrl',
				templateUrl:'views/loginView.html'
			}
		)
		.when(
			'/login/:user/:pwd',
			{
				controller:'ReLoginCtrl',
				resolve:
				{
					currentUser:
					[
						'LoginService',
						function(LoginService)
						{
							//alert('ctrl');
							return LoginService();
						}
					]
				},
				templateUrl:'views/loginView.html'
			}
		)
		.when(
			'/users',
			{
				controller: 'UserListCtrl',
				resolve: 
				{
					users: 
					[
						"MultiUserLoader", 
						function(MultiUserLoader)
						{
							return MultiUserLoader();
						}
					]
				},
				templateUrl:'views/userlist.html'
			}
		)
		
		.when(
			'/records/applicant/:ApplicantID/status/:Status/time/:Time',
			{
				controller: 'RecordListCtrl',
				resolve: {
					records: 
					[
						"MultiRecordLoader", 
						function(MultiRecordLoader) {
							return MultiRecordLoader();
						}
					]
				},
				templateUrl:'views/recordlist.html'
			}
		)
		.when(
			'/records',
			{
				redirectTo:'/records/applicant/' + currentUserG.id + '/status/all/time/all'//not completed
			}
		)
		.when(
			'/view/user/:UserId', 
			{
				controller: 'UserViewCtrl',
				resolve: {
					user: ["UserLoader", function(UserLoader) {
						return UserLoader();
					}]
				},
				templateUrl:'views/viewUser.html'
			}
		)
		.when(
			'/edit/user/:UserId',
			{
				controller:'UserEditCtrl',
				resolve:{
					user: ["UserLoader", function(UserLoader) {
						return UserLoader();
					}],
					departments:["MultiDepartmentLoader" ,function(MultiDepartmentLoader){
						return MultiDepartmentLoader();
					}]
				},
				templateUrl:'views/editUser.html'
			}
		)
		.when(
			'/new/user',
			{
				controller:'UserNewCtrl',
				resolve:{
					departments:["MultiDepartmentLoader" ,function(MultiDepartmentLoader){
						return MultiDepartmentLoader();
					}]
				},
				templateUrl:'views/newUser.html'
			}
		)
		.when(
			'/404',
			{
				templateUrl:'views/404.html'
			}
		)
		.when(
			'/500',
			{
				templateUrl:'views/500.html'
			}
		)
		/*
		.when('/new', {
			controller: 'NewCtrl',
			templateUrl:'views/recipeForm.html'
		})*/
		.otherwise({redirectTo:'/404'});
	}
]);

app.controller(
	'ReLoginCtrl',
	[
		'$scope',
		'$location',
		'currentUser',
		function($scope,$location,currentUser){
			if(currentUser.id == null){
				alert("login failed");
			}
			else{
				currentUserG=currentUser;
				SetCookie("u_id", currentUser.id);
				SetCookie("u_name",currentUser.name);
				SetCookie("u_department",currentUser.department_id);
				SetCookie("u_position", currentUser.position);
				$scope.currentUser = currentUser;
				alert('User log in successful.' + '\nHello ' + currentUserG.name)
				$location.path('/records/applicant/' + currentUserG.id + '/status/all/time/all');
			}
			$scope.login = function(){
				$location.path('/login/'+ currentUser.id + '/' + currentUser.pwd);
			};
		}
	]
)
app.controller(
	'LoginCtrl',
	[
		'$scope',
		'$location',
		function($scope,$location){
			$scope.currentUser = currentUserG;
			var currentUser = currentUserG;
			$scope.login = function(){
				$location.path('/login/'+ currentUser.id + '/' + currentUser.pwd);
			};
		}
	]
)
app.controller(
	'UserListCtrl', 
	[
		'$scope', 
		'users',
		function($scope, users) {
			if( users.response!=null)
				$scope.users = [];
			else
				$scope.users = users;
			
		}
	]
);
app.controller(
	'RecordListCtrl', 
	[
		'$scope', 
		'records',
		function($scope, records) {
			/*if( records[0]==null || records[0].applyID == null)
				$scope.records = [];
			else*/
				$scope.records = records;
		}
	]
);
app.controller(
	'UserViewCtrl', 
	[
		'$scope', '$location', 'user',
		function($scope, $location, user) 
		{
			$scope.user = user;
			$scope.edit = function() 
				{
					if(currentUserG.position.toUpperCase()=="ADMIN"||currentUserG.id == user.user_id)
						$location.path('/edit/user/' + user.user_id);
					else alert("You have no authority to get access!");
				};
		}
	]
);

app.controller('UserEditCtrl',
	[
		'$scope', '$location', 'user','departments',
		function($scope, $location, user, departments) {
			$scope.user = user;
			var positionSelect = [{"value":"employee"},{"value":"manager"},{"value":"vice general manager"},{"value":"general manager"}];
			$scope.departments = departments;
			$scope.positionSelect = positionSelect;
			$scope.save = function() {
				user.$save(
					function(user) {
						$location.path('/view/user/' + user.user_id);
					}
				);
			};

			$scope.remove = function() {
				$scope.user.$delete(function(){});
				$location.path('/users');
			};
		}
	]
);

app.controller('UserNewCtrl',
	[
		'$scope', '$location','User', 'departments',
		function($scope, $location, User,  departments) {
			var user = new User({});
			$scope.user = user;
			var positionSelect = [{"value":"employee"},{"value":"manager"},{"value":"vice general manager"},{"value":"general manager"}];
			$scope.departments = departments;
			$scope.positionSelect = positionSelect;
			$scope.save = function() {
				user.$save(
					function(user) {
						$location.path('/view/user/' + user.user_id);
					}
				);
			};

			$scope.remove = function() {
				$scope.user.$delete(function(){});
				$location.path('/users');
			};
		}
	]
);

app.controller('NewCtrl', ['$scope', '$location', 'Recipe',
    function($scope, $location, Recipe) {
  $scope.recipe = new Recipe({
    ingredients: [ {} ]
  });

  $scope.save = function() {
    $scope.recipe.$save(function(recipe) {
    $location.path('/');
    });
  };
}]);
/*
app.controller('IngredientsCtrl', ['$scope',
    function($scope) {
  $scope.addIngredient = function() {
    var ingredients = $scope.recipe.ingredients;
    ingredients[ingredients.length] = {};
  };
  $scope.removeIngredient = function(index) {
    $scope.recipe.ingredients.splice(index, 1);
  };
}]);*/
