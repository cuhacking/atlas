//
//  SearchViewModelWrapper.swift
//  atlas
//
//  Created by Safa on 2021-07-12.
//  Copyright © 2021 cuHacking. All rights reserved.
//

import Combine
import Common

class SearchViewModelWrapper: ObservableObject {

    let viewModel: SearchViewModel
    @Published var searchResults: Array<SearchResult>

    init() {
        searchResults = []
        viewModel = SearchViewModel(dispatchers: CoroutineDispatchers(), scope: MainScope(), withDatabase: PopulateDatabaseKt.sharedDatabase)
        
        viewModel.searchResults.subscribe(
            onEvent: { result in
                self.searchResults = result as! Array<SearchResult>
            },
            onError: {throwable in
                print("SearchViewModelWrapper onError: " + throwable.description())
            },
            onComplete: {
                print("SearchViewModelWrapper onComplete")
            }
        )
    }
    
    func getSearchResults(query: String) {
        viewModel.getSearchResults(query: query)
    }
}
