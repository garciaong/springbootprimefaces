var csrfTest = new Vue({
	el: '#csrf-test',
	data: {
		info: '',
		result: 'You should see result here once posted...'
	},
	methods: {
        formSubmit(e) {
        	axios.defaults.headers.common = {
        		    'X-Requested-With': 'XMLHttpRequest',
        		    'X-CSRF-TOKEN' : document.querySelector('meta[name="X-CSRF-TOKEN"]').getAttribute('content')
        		};
        	
            e.preventDefault();
            var csrfObj = this;
            axios.post('http://localhost:8080/testcsrf', {
                info: this.info
            })
            .then(function (response) {
            	csrfObj.result = response.data;
            })
            .catch(function (error) {
            	csrfObj.result = error;
            });
        }
    }
});	
