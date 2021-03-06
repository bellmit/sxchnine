import React, {Component} from 'react';
import Aux from '../../hoc/Aux/Aux';
import {Grid, Dropdown} from 'semantic-ui-react';
import { connect } from 'react-redux';
import './HomeSearch.css';
import * as actions from "../../store/actions";


class HomeSearch extends Component {

    state= {
        gender: '',
        category: '',
        size: ''
    };

    handleChangeGender = (e, { value }) => this.setState({ value, gender: value});
    handleChangeCategories = (e, { value }) => this.setState({ value, category: value });
    handleChangeSize = (e, { value }) => this.setState({ value, size: value });

    searchAdvanced = () => {
        this.props.homeSearchProducts(this.state.gender, this.state.category, this.state.size, this.props.history);
    };

    render(){

        const gender = [
            { key: 1, text: 'No Gender', value: 'No' },
            { key: 2, text: 'Women', value: 'W' },
            { key: 3, text: 'Men', value: 'M' },
        ];

/*        const categories = [
            { key: 1, text: 'T-Shirt', value: 't-shirt' },
            { key: 2, text: 'Sweat', value: 'sweat' },
            { key: 3, text: 'Jacket', value: 'jacket' },
            { key: 4, text: 'Hoodie', value: 'hoodie' },
        ];*/

        const size = [
            {key: '1', text: 'Small', value: 's'},
            {key: '2', text: 'Medium', value: 'm'},
            {key: '3', text: 'Large', value: 'l'},
            {key: '4', text: 'XL', value: 'xl'},
        ];


        return (
          <Aux>
            <Grid relaxed columns='equal'>
                <Grid.Column>
                    <Dropdown className="Home-Search-Advanced"
                              onChange={this.handleChangeGender}
                              options={gender}
                              placeholder='Gender'
                              selection
                              value={this.state.gender} />
                </Grid.Column>
               {/* <Grid.Column width={4}>
                    <Dropdown className="Home-Search-Advanced"
                              onChange={this.handleChangeCategories}
                              options={categories}
                              placeholder='Categories'
                              selection
                              value={this.state.category} />
                </Grid.Column>*/}
                <Grid.Column>
                    <Dropdown className="Home-Search-Advanced"
                              onChange={this.handleChangeSize}
                              options={size}
                              placeholder='Size'
                              selection
                              value={this.state.size} />
                </Grid.Column>
                <Grid.Column >
                    <button className="Home-Search-Continue-Button" onClick={this.searchAdvanced}>
                        <span className="Home-Search-Text-Button">GOT IT -></span>
                    </button>
                </Grid.Column>
            </Grid>
          </Aux>
        );
    }
}

const mapDispatchToProps = dispatch => {
    return {
        homeSearchProducts: (gender, category, size, history) => dispatch(actions.homeSearchProducts(gender, category, size, history))
    }
}


export default connect(null, mapDispatchToProps)(HomeSearch);