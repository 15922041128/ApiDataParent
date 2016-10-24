var input = document.getElementsByTagName("input");
		if (input != null) {
			for ( var i = 0; i < input.length; i++) {
				if (input[i].type == "text" || input[i].type == "password") {
					input[i].autocomplete = "off";
				}
			}
		}