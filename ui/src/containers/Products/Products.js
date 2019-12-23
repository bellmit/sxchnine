import React, {Component} from "react";
import {Dimmer, Form, Grid, Icon, Input, Loader, Segment} from 'semantic-ui-react';
import { Collapse } from "@chakra-ui/core";
import { connect } from 'react-redux';
import * as actions from '../../store/actions/index';
import Product from './Product';
import BannerMen from '../../components/Banner/Banner';
import Contact from '../Contact/Contact';
import ShopResume from "../ShopResume/ShopResume";
import './Products.css';
import addIcon from './add-icon.jpg';
import searchIcon from './searc-icon-tiny.png';

class Products extends Component {
    state = {
        change: '',
        show: false,
        size: 0
    }

    componentDidMount() {
        console.log("Products.js " + this.props);
        this.props.loadProducts();
        this.props.loadGender();
        this.props.loadTypes();
        this.props.loadSize();
    }

    changeHandler = (event) => {
        this.setState({
            change: event.target.value
        })
    }

    selectProductHandler = (id) => {
        this.props.loadProduct(id, this.props.history);

/*        if (!this.props.error){
            this.props.history.push('/products/' + id);
        }*/
    }

    toggleAdvancedSearch = () => {
        this.setState((state) => ({
            show: !state.show
        }))
    }

    render() {
        return (
            <div>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading' />
                </Dimmer>
                <div className="Products-Yellow-bar-div"/>
                <div>
                    <header>
                        <BannerMen {...this.props}/>
                    </header>
                </div>
                <div>
                    <ShopResume size = {0} {...this.props}/>
                </div>

                <div className="Product-Message">
                    Do you wanna got some?
                </div>
                <div className="Product-Search-Input">
                    <Segment inverted size='mini'>

                        <Input inverted icon={<Icon name='search' inverted circular link/>}
                               placeholder='Search...'
                               onChange={this.changeHandler} style={{marginBottom: '10px'}}/>

                        <img alt="" src={addIcon} className="Add-Icon" onClick={this.toggleAdvancedSearch}/>
                        <Collapse isOpen={this.state.show}>
                            <Form size='tiny' mobile={2}>
                                <Form.Group inline widths='1' unstackable mobile={2}>
                                    <Form.Select
                                        width={6}
                                        options={this.props.gender}
                                        placeholder='SEXE'
                                        className="Product-Search-Advanced"/>
                                    <Form.Select
                                        width={6}
                                        options={this.props.types}
                                        placeholder='Genre'
                                        className="Product-Search-Advanced"/>
                                    <Form.Select
                                        width={6}
                                        options={this.props.size}
                                        placeholder='Size'
                                        className="Product-Search-Advanced"/>
                                    <img alt="" src={searchIcon} className="Search-Icon"/>
                                </Form.Group>
                            </Form>
                        </Collapse>
                    </Segment>
                </div>
                <div>{this.state.change}</div>

                <div className="Product-Container">
                    <Grid centered columns={3}>
                        <Grid.Row centered>

                            {this.props.products.map((product, index) => (
                                <Grid.Column key={index} mobile={16} tablet={8} computer={5} centered="true" >
                                    <Product name={product.name}
                                             id={product.id}
                                             clicked={() => this.selectProductHandler(product.id)}/>
                                    <br/>
                                </Grid.Column>

                            ))}
                        </Grid.Row>
                    </Grid>
                    <div className="Product-Empty-Div"/>

                    <div className="Product-footer">
                        <Contact/>
                    </div>
                </div>

            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        products: state.products.products,
        gender: state.products.gender,
        types: state.products.types,
        size: state.products.size,
        loading: state.product.loading,
        product: state.product.product,
        error: state.product.error
    }
}

const mapDispatchToProps = dispatch => {
    return {
        loadProducts: () => dispatch(actions.loadProducts()),
        loadGender: () => dispatch(actions.loadGenders()),
        loadTypes: () => dispatch(actions.loadTypes()),
        loadSize: () => dispatch(actions.loadSize()),
        loadProduct: (id, history) => dispatch(actions.loadProduct(id, history)),
    }
}



export default connect (mapStateToProps, mapDispatchToProps) (Products);