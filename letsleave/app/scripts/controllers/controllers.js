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
				redirectTo:'/records/applicant/' + getCookie("u_id") + '/status/all/time/all'//not completed
			}
		)
		.when(
			'/view/record/:application_id',
			{
				controller:'RecordViewCtrl',
				resolve:{
					application:[
						"ApplicationLoader",
						function(ApplicationLoader){
							return ApplicationLoader();
						}
					]
				},
				templateUrl:'views/recordView.html'
			}
		)
		.when(
			'/edit/record/:application_id',
			{
				controller:'RecordEditCtrl',
				resolve:{
					application:[
						"ApplicationLoader",
						function(ApplicationLoader){
							return ApplicationLoader();
						}
					]
				},
				templateUrl:'views/editApplication.html'
			}
		)
		.when(
			'/new/record',
			{
				controller:'RecordNewCtrl',
				templateUrl:'views/editApplication.html'
			}
		)
		.when(
			'/approve/:application_id',
			{
				controller:'ApproveCtrl',
				resolve:{
					application:[
						"ApplicationLoader",
						function(ApplicationLoader){
							return ApplicationLoader();
						}
					]
				},
				templateUrl:'views/approve.html'
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
		'$route',
		function($scope,$location,currentUser,$route){
			if(currentUser.id == null){
				alert("login failed");
				$scope.currentUser = currentUserG;
				$location.path('/login');
			}
			else{
				currentUserG=currentUser;
				SetCookie("u_id", currentUser.id);
				SetCookie("u_name",currentUser.name);
				SetCookie("u_department",currentUser.department_id);
				SetCookie("u_position", currentUser.position);
				$scope.currentUser = currentUser;
				alert('User log in successful.' + '\nHello ' + currentUserG.name)
				$route.reload();
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
			alertErr(users);
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
			alertErr(records);
			$scope.records = records;
		}
	]
);

app.controller(
	'RecordViewCtrl',
	[
		'$scope','application', '$location',
		function($scope, application, $location){
			alertErr(application, $location);
			$scope.application = application;
			if(application.status=="DENIED"||application.status=="PASSED"){
				$scope.needApprove=false;
			}
			else $scope.needApprove=true;
			$scope.edit = function() 
			{
				if(currentUserG.id==application.applicant_id&&application.status=="WAITDM")
					$location.path('/edit/record/' + application.application_id);
				else alert("You have no authority to get access!");
			};
			$scope.approve = function(){
				if(currentUserG.position!="employee")
					$location.path("/approve/" + application.application_id);
				else alert("You have no authority to approve this.");
			}
		}
	]
);

app.controller(
	'UserViewCtrl', 
	[
		'$scope', '$location', 'user',
		function($scope, $location, user) 
		{
			alertErr(user, $location);
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
app.controller(
	'RecordEditCtrl',
	[
		'$scope', '$location', 'application',
		function($scope, $location, application) {
			alertErr(application, $location);
			$scope.invisible = false;
			$scope.application = application;
			$scope.user_id = getCookie("u_id");
			$scope.leavetype=[{"value":"sick"},{"value":"annual"},{"value":"go out"},{"value":"marital"},{"value":"maternity"},{"value":"others"}];
			$scope.save = function() {
				application.$save(
					function(application) {
						$location.path('/view/record/' + application.application_id);
					}
				);
			};

			$scope.remove = function() {
				$scope.application.$delete(function(){});
				$location.path('/records');
			};
		}
	]
);
app.controller(
	'ApproveCtrl',
	[
		'Approval', '$scope', '$location', 'application',
		function(Approval, $scope, $location, application) {
			alertErr(application, $location);
			$scope.application = application;
			$scope.auditor_id = getCookie("u_id");
			var approval = new Approval({});
			$scope.approval = approval;
			$scope.approval.application_id = application.application_id;
			$scope.save = function() {
				application.$save(
					function(application) {
						$location.path('/view/record/' + application.application_id);
					}
				);
			};
		}
	]
)
app.controller('UserEditCtrl',
	[
		'$scope', '$location', 'user','departments',
		function($scope, $location, user, departments) {
			alertErr(user, $location);
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
app.controller('RecordNewCtrl',
	[
		'$scope', '$location','Application',
		function($scope, $location, Application) {
			var application = new Application({});
			$scope.application = application;
			$scope.leavetype=[{"value":"sick"},{"value":"annual"},{"value":"go out"},{"value":"marital"},{"value":"maternity"},{"value":"others"}];
			$scope.user_id = getCookie("u_id");
			$scope.save = function() {
				application.$save(
					function(application) {
						$location.path('/records');
					}
				);
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
		}
	]
);

function alertErr(res, $location){
	try{
		if(res[0].response!=null){
			
			$location.path("/500");
			alert(res[0].response);
		}
	}catch(e){
		
	}
	return;
}
function alertErr(res){
	try{
		if(res[0].response!=null){
			alert(res[0].response);
		}
	}catch(e){
		
	}
	return;
}
