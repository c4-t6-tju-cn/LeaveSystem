'use strict';

var app = angular.module(
			'leavesystem',
			[
				'leavesystem.directives', 
				'leavesystem.services'
			]
		);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
		.when(
			'/', 
			{
				redirectTo:'/users'
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
				redirectTo:'/records/applicant/all/status/all/time/all'//not completed
			}
		)
		/*.when('/edit/:recipeId', 
			{
				controller: 'EditCtrl',
				resolve: {
					recipe: ["RecipeLoader", function(RecipeLoader) {
					return RecipeLoader();
				}]
			},
			templateUrl:'views/recipeForm.html'
		})
		*/
		.when(
			'/view/user/:UserId', 
			{
				controller: 'UserViewCtrl',
				resolve: {
					user: ["UserLoader", function(UserLoader) {
						
						return UserLoader();
					}]
				},
				templateUrl:'views/viewuser.html'
		})
		/*
		.when('/new', {
			controller: 'NewCtrl',
			templateUrl:'views/recipeForm.html'
		}).otherwise({redirectTo:'/'})*/;
	}
]);

app.controller(
	'UserListCtrl', 
	[
		'$scope', 
		'users',
		function($scope, users) {
			if( users[0]==null || users[0].name == null)
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
					$location.path('/edit/' + user.id);
				};
		}
	]
);
/*
app.controller('EditCtrl', ['$scope', '$location', 'recipe',
    function($scope, $location, recipe) {
  $scope.recipe = recipe;
  
  $scope.save = function() {
    $scope.recipe.$save(function(recipe) {
      $location.path('/view/' + recipe.id);
    });
  };

  $scope.remove = function() {
    delete $scope.recipe;
    $location.path('/');
  };
}]);

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
