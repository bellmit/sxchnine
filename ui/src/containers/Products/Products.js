import React, {Component} from "react";
import {Dimmer, Form, Grid, Icon, Input, Loader, Segment} from 'semantic-ui-react';
import {Collapse} from "@chakra-ui/core";
import {connect} from 'react-redux';
import {Waypoint} from "react-waypoint";
import Aux from '../../hoc/Aux/Aux';
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
        size: 0,
        count: 0
    }

    componentDidMount() {
        console.log("Products.js ");
        console.log(this.props);

        this.props.loadProducts(0, 9);
        this.props.loadGender();
        this.props.loadTypes();
        this.props.loadSize();
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        console.log('Products.js did update');
    }

    changeHandler = (event) => {
        this.setState({
            change: event.target.value
        })
    };

    selectProductHandler = (id) => {
        this.props.loadProduct(id, this.props.history);
    };

    toggleAdvancedSearch = () => {
        this.setState((state) => ({
            show: !state.show
        }))
    };

    fetchMore = (index) => {
        console.log('fetchMore');
        if (this.props.products.length < 27) {
            this.setState((prev) => ({
                count: prev.count + 1
            }));
            this.props.loadProducts(this.state.count, 9);
        }
        console.log(this.props.products);
    }

    render() {
        return (
            <Aux>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div className="Products-Yellow-bar-div"/>
                <div>
                    <header>
                        <BannerMen {...this.props}/>
                    </header>
                </div>
                <div>
                    <ShopResume {...this.props}/>
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
                <div className="Product-Container">
                    <Grid centered columns={3} textAlign="center" padded="vertically">
                        <Grid.Row centered>

                            {this.props.products.map((product, index) => (
                                <Grid.Column key={index} mobile={16} tablet={8} computer={5} centered="true">
                                    <Aux>
                                        <Product name={product.name}
                                                 image={product.images}
                                                 logo={product.logo}
                                                 brand={product.brand}
                                                 price={product.price}
                                                 size={product.size}
                                                 id={product.id}
                                                 height="80%"
                                                 width="80%"
                                                 clicked={() => this.selectProductHandler(product.id)}/>

                                        {index === this.props.products.length - 1
                                        && (<Waypoint onEnter={() => this.fetchMore(index)}/>)}
                                        <br/>
                                    </Aux>
                                </Grid.Column>

                            ))}
                        </Grid.Row>
                    </Grid>
                    <div className="Product-Empty-Div"/>

                    <div className="Product-footer">
                        <Contact/>
                    </div>
                </div>

            </Aux>
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
        loadProducts: (pageNo, pageSize) => dispatch(actions.fetchProduct(pageNo, pageSize)),
        loadGender: () => dispatch(actions.loadGenders()),
        loadTypes: () => dispatch(actions.loadTypes()),
        loadSize: () => dispatch(actions.loadSize()),
        loadProduct: (id, history) => dispatch(actions.loadProduct(id, history)),
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(Products);