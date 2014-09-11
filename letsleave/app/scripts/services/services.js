'use strict';

var services = angular.module('leavesystem.services',
    ['ngResource']);

	
/* User elements */
services.factory(
	'User', 
	[
		'$resource',
		function($resource)
		{
			return $resource('/LeaveSystem/services/user/:id', {id: '@id'});
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
				'/LeaveSystem/services/record/applyID/:applyID/status/:status/time/:time', 
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