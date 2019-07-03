Vue.component('row-item', {
	props: ['row'],
	template: '<div class="columns"><div class="column">{{ row.content }}</div></div>'
});

var app7 = new Vue({
	el: '#app',
	data: {
		myList: [
			{ id: 0, content: 'Vegetables' },
			{ id: 1, content: 'Cheese' },
			{ id: 2, content: 'Whatever else humans are supposed to eat' }
		]
	}
});

//computed same as methods but only it cache the data until detected changes
var vm = new Vue({
  el: '#example',
  data: {
    message: '12345'
  },
  computed: {
    // a computed getter
    reversedMessage: function () {
      // `this` points to the vm instance
      return this.message.split('').reverse().join('')
    }
  }
});

var vm = new Vue({
	el: '#hiddenMsg',
	data: {
		message: 'original'
	},
	methods: {
		myMethod: function () {
			return this.message='hidden'
		}
	}
});

var watchExampleVM = new Vue({
	el: '#watch-example',
	data: {
		question: '',
		answer: 'I cannot give you an answer until you ask a question!'
	},
	watch: {
		// whenever question changes, this function will run
		question: function (newQuestion, oldQuestion) {
		this.answer = 'Waiting for you to stop typing...'
		this.debouncedGetAnswer()
		}
	},
	created: function () {
		// _.debounce is a function provided by lodash to limit how
		// often a particularly expensive operation can be run.
		// In this case, we want to limit how often we access
		// yesno.wtf/api, waiting until the user has completely
		// finished typing before making the ajax request. To learn
		// more about the _.debounce function (and its cousin
		// _.throttle), visit: https://lodash.com/docs#debounce
		this.debouncedGetAnswer = _.debounce(this.getAnswer, 500)
	},
	methods: {
		getAnswer: function () {
		if (this.question.indexOf('?') === -1) {
			this.answer = 'Questions usually contain a question mark. ;-)'
			return
		}
		this.answer = 'Thinking...'
		var vm = this
		axios.get('https://yesno.wtf/api')
			.then(function (response) {
				vm.answer = _.capitalize(response.data.answer)
			})
			.catch(function (error) {
				vm.answer = 'Error! Could not reach the API. ' + error
			})
		}
	}
});
