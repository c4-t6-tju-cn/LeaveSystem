'use strict';

var services = angular.module('leavesystem.services',
    ['ngResource']);

var services_map = '/LeaveSystem/services'
/* User elements */
services.factory(
	'User', 
	[
		'$resource',
		function($resource)
		{
			return $resource( services_map + '/user/:userID', {userID: '@user_id'});
		}
	]
);

services.factory(
	'Department', 
	[
		'$resource',
		function($resource)
		{
			return $resource( services_map + '/department', {});
		}
	]
);
services.factory(
	'Approval', 
	[
		'$resource',
		function($resource)
		{
			return $resource( services_map + '/approval', {});
		}
	]
);
services.factory(
	'MultiDepartmentLoader', 
	[
		'Department', '$q',
		function(Department, $q, $location) 
		{
			return function() 
			{
				var delay = $q.defer();
				Department.query(
					function(Deps) 
					{
						delay.resolve(Deps);
					}, 
					function() 
					{
						$location.path("/500");
						delay.reject('Unable to fetch Users');
					}
				);
				return delay.promise;
			};
		}
	]
);
services.factory(
	'ApplicationRecordQueryList', 
	[
		'$resource',
		function($resource)
		{
			return $resource(
				services_map + '/record/applyID/:applyID/status/:status/time/:time', 
				{
					applyID:'@applyID',
					status:'@status',
					time:'@time'
				},
				{
					'get':{	
						method:'GET',
						isArray:true
					}
				}
			);
		}
	]
);

services.factory(
	'Application',
	[
		'$resource',
		function($resource){
			return $resource(
				services_map + '/record/:application_id',
				{
					application_id:'@application_id'
				}
			);
		
		}
	]
);

services.factory(
	'ApplicationLoader',
	[
		'Application', '$route', '$q',
		function(Application, $route, $q){
			return function()
			{
				var delay = $q.defer();
				Application.get(
					{application_id:$route.current.params.application_id},
					function(Application) 
					{
						delay.resolve(Application);
					}, 
					function() 
					{
					
						delay.reject('Unable to fetch Application');
					}
				);
				return delay.promise;
			}
		}
	]
)

services.factory(
	'MultiUserLoader', 
	[
		'User', '$q',
		function(User, $q) 
		{
			return function() 
			{
				var delay = $q.defer();
				User.query(
					function(Users) 
					{
						delay.resolve(Users);
					}, 
					function() 
					{
						delay.reject('Unable to fetch Users');
					}
				);
				return delay.promise;
			};
		}
	]
);

services.factory(
	'UserLoader', 
	[
		'User', '$route', '$q',
		function(User, $route, $q) 
		{
			return function() 
			{
				var delay = $q.defer();
				User.get(
					{userID: $route.current.params.UserId}, 
					function(User) 
					{
						//alert($route.current.params.userId);
						delay.resolve(User);
					}, 
					function() 
					{
						delay.reject('Unable to fetch User '  + $route.current.params.UserId);
					}
				);
				return delay.promise;
			};
		}
	]	
);

/*	Application record elements */




services.factory(
	'MultiRecordLoader', 
	[
		'ApplicationRecordQueryList', '$route', '$q','$location',
		function(ApplicationRecordQueryList, $route, $q, $location) 
		{
			return function() 
			{
				var delay = $q.defer();
				ApplicationRecordQueryList.get(
					{
						applyID: $route.current.params.ApplicantID,
						status: $route.current.params.Status,
						time: $route.current.params.Time
					}, 
					function(Records) 
					{
						//alert('x');
						delay.resolve(Records);
						
					}, 
					function() 
					{
						
						delay.reject('Unable to fetch Records');
						$location.path("/500");
					}
				);
				
				return delay.promise;
			};
		}
	]
);

services.factory(
	'LoginUser', 
	[
		'$resource',
		function($resource)
		{
			return $resource( services_map + '/login', {});
		}
	]
);
services.factory(
	'LoginService', 
	[
		'LoginUser', '$route', '$q','$location',
		function(LoginUser, $route, $q, $location){
			return function()
				{
				var delay = $q.defer();
				LoginUser.get(
					{
						user: $route.current.params.user,
						pwd: $route.current.params.pwd,
					}, 
					function(currentUser){
						//alert('x');
						delay.resolve(currentUser);
						
					}, 
					function(){
						//alert('z');
						
						delay.reject('Unable to fetch Records');
						$location.path("/500");
					}
				);
				return delay.promise;
			};
		}
	]
);