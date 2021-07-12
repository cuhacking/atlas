//
//  SearchView.swift
//  atlas
//
//  Created by Safa on 2021-07-11.
//  Copyright © 2021 cuHacking. All rights reserved.
//

import SwiftUI
import Common

struct SearchView: View {
    
    @ObservedObject var viewModel = SearchViewModelWrapper()
    @Binding var isSearchResultsVisible: Bool

    var body: some View {
        ZStack(alignment: .top) {
            SearchResultList(isVisible: $isSearchResultsVisible, results: viewModel.searchResults)
            SearchBar(isSearchResultsVisible: $isSearchResultsVisible, viewModel: viewModel)
        }
        .padding(15)
    }
}

struct SearchBar: View {
    
    @State var query: String = "" {
        didSet {
            viewModel.getSearchResults(query: query)
        }
    }
    @Binding var isSearchResultsVisible: Bool
    var viewModel: SearchViewModelWrapper

    var body: some View {
        let queryBinding = Binding<String>(
            get: { self.query },
            set: {
                self.query = $0
                viewModel.getSearchResults(query: query)
                self.isSearchResultsVisible = true
                print(self.query)
                print(viewModel.searchResults)
            }
        )
        
        HStack {
            Image(systemName: "magnifyingglass")
                .padding(.leading, 5)
            Spacer()
            TextField("Search...", text: queryBinding)
                .frame(minHeight: 40)
                .padding(.leading, 3)
            
            VStack(alignment: .trailing) {
                if query != "" {
                    Button(action: {query = ""}, label: {
                        Image(systemName: "xmark.circle.fill")
                            .padding(.trailing, 10)
                            .foregroundColor(.gray)
                    })
                    .transition(.move(edge: .trailing))
                    .animation(.spring())
                }
            }
        }
        .background(Color(.systemGray5))
        .cornerRadius(3)
        .border(Color(.darkGray))
    }
}

struct SearchResultView: View {
    
    var name: String
    var description: String
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(name)
                    .font(.system(size: 15.0))
                Text(description)
                    .font(.system(size: 13.0))
                    .fontWeight(.light)
                    .foregroundColor(Color(.darkGray))
                Divider().background(Color(.darkGray))
            }
            .padding(.leading, 7)
            Spacer()
        }
    }
}

struct SearchResultList: View {
    
    @Binding var isVisible: Bool
    var results: Array<SearchResult>
    
    var body: some View {
        if(isVisible) {
            ScrollView {
                VStack {
                    ForEach(results, id: \.self) { result in
                        SearchResultView(name: result.name, description: result.description_)
                    }
                }
                .padding(.top, 10)
            }
            .frame(maxHeight: 155)
            .background(Color(.systemGray5))
            .cornerRadius(3)
            .padding(.top, 35)
            .padding([.leading, .trailing], 1)
        }
    }
}
