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
			return $resource( services_map + '/user/:id', {id: '@id'});
		}
	]
);



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
					{id: $route.current.params.UserId}, 
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
	'MultiRecordLoader', 
	[
		'ApplicationRecordQueryList', '$route', '$q',
		function(ApplicationRecordQueryList, $route, $q) 
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
						//alert('z');
						delay.reject('Unable to fetch Records');
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
		'LoginUser', '$route', '$q',
		function(LoginUser, $route, $q){
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
					}
				);
				return delay.promise;
			};
		}
	]
);