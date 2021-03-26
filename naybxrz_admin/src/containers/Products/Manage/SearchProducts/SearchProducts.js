import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Dimmer, Form, Loader, Segment} from "semantic-ui-react";
import * as actions from '../../../../store/actions/index';
import loop from './loop.png';
import './SearchProducts.css';

class SearchProducts extends Component {

    state = {
        productId: '',
        productName: '',
        brand: '',
        sex: ''
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    handleChangeSex = (e, { value }) => this.setState({ value, sex: value });

    searchProducts = () => {
        this.props.searchProductsData(this.state.productId, this.state.productName, this.state.brand, this.state.sex);
    }


    render() {

        let sexOptions = [
            { key: 0, text: '----', value: '' },
            { key: 1, text: 'Men', value: 'M' },
            { key: 2, text: 'Women', value: 'W' },
        ]


        return (
            <div className="manage-div">
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <Segment inverted color='yellow' className="products-segment-div">
                    <Form size='small'>
                        <Form.Group widths='1'>
                            <Form.Input inverted
                                        size='mini'
                                        placeholder='Product ID'
                                        name='productId'
                                        value={this.state.productId}
                                        onChange={this.handleChange}/>
                            <Form.Input inverted
                                        size='mini'
                                        placeholder='Product Name'
                                        name='productName'
                                        value={this.state.productName}
                                        onChange={this.handleChange}/>
                            <Form.Input inverted
                                        size='mini'
                                        placeholder='Brand Name'
                                        name='brand'
                                        value={this.state.brand}
                                        onChange={this.handleChange}/>
                            <Form.Dropdown className="products-dropdown-sex"
                                onChange={this.handleChangeSex}
                                size='mini'
                                fluid
                                selection
                                options={sexOptions}
                                value={this.state.sex}
                                placeholder='Size'/>

                            <img alt="search" src={loop} className="products-search-loop"
                                 onClick={this.searchProducts}/>
                        </Form.Group>
                    </Form>
                </Segment>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.products.searchProductsLoading
    }
};

const dispatchToProps = dispatch => {
    return {
        searchProductsData: (productId, productName, brand, sex) => dispatch(actions.searchProducts(productId, productName, brand, sex))
    }
}

export default connect(mapStateToProps, dispatchToProps)(SearchProducts);