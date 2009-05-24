class UrlMappings {
    static mappings = {

        "/$controller/$action?/$id?"{
	    constraints {
		// apply constraints here
                controller( matches: /.*[^(services)].*/ )
	    }
	}

	"500"(view:'/error')
    }
}
