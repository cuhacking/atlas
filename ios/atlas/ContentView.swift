//
//  ContentView.swift
//  atlas
//
//  Created by Derek Ellis on 2020-06-28.
//  Copyright © 2020 cuHacking. All rights reserved.
//

import SwiftUI
import Mapbox
import Common

struct ContentView: View {

    @ObservedObject var viewModel = ViewModel()
    @State var isSearching = false

    var body: some View {

        VStack {
            SearchBar(viewModel: viewModel, isSearching: $isSearching)
            // Search results display
            if isSearching {
                ScrollView {
                    ForEach(
                        viewModel.filteredData,
                        id: \.self) { str in

                        HStack {
                            Text(str)
                            Spacer()
                        }.padding()

                        Divider()
                            .background(Color(.gray))
                            .padding(.leading)
                        }
                }.frame(minWidth: /*@START_MENU_TOKEN@*/0/*@END_MENU_TOKEN@*/, idealWidth: .infinity, maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/,
                        minHeight: /*@START_MENU_TOKEN@*/0/*@END_MENU_TOKEN@*/, idealHeight: 200, maxHeight: .infinity,
                        alignment: .leading)
                .background(Color(.systemGray5))
                .foregroundColor(.gray)
            }
            MapView().centerCoordinate(.init(latitude: 45.386438, longitude: -75.696127)).zoomLevel(14.661)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

struct SearchBar: View {

    @ObservedObject var viewModel: ViewModel
    @Binding var isSearching: Bool

    var body: some View {
        HStack {
            HStack {
                TextField("Search for a location...", text: $viewModel.searchText)
                    .padding(.leading, 24)

            }.padding()
            .padding(.horizontal)
            .cornerRadius(6)
            .background(Color(.systemGray5))
            .overlay(
                HStack {
                    Image(systemName: "magnifyingglass")
                    Spacer()
                    if isSearching {
                        Button(action: {viewModel.searchText = ""}, label: {
                            Image(systemName: "xmark.circle.fill")
                                .padding(.vertical)
                        })
                    }
                }.padding(.horizontal)
                .foregroundColor(.gray)
            ).onTapGesture(perform: {
                isSearching = true
            })
            .transition(.move(edge: .trailing))
            .animation(.spring())

            if isSearching {
                Button(action: {
                    isSearching = false
                    viewModel.searchText = ""

                    UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)

                }, label: {
                    Text("Cancel")
                        .padding(.trailing)
                })
                .transition(.move(edge: .trailing))
                .animation(.spring())
            }
        }
    }
}
