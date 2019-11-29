import React, {Component} from "react";
import {Grid, Icon, Input, Segment} from 'semantic-ui-react'
import Product from './Product';
import BannerMen from '../../components/Banner/Banner';
import Contact from '../../components/Contact/Contact';
import ShopResume from "../ShopResume/ShopResume";
import './Products.css';

class Products extends Component {
    state = {
        products: [
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 2, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 3, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 4, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 5, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 6, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },

        ],
        change: '',
        size: 0
    }

    changeHandler = (event) => {
        this.setState({
            change: event.target.value
        })
    }

    selectProductHandler = (id) => {
        this.props.history.push('/products/' + id);
    }

    render() {

        return (
            <div>
                <div>
                    <header>
                        <BannerMen/>
                    </header>
                </div>
                <div>
                    <ShopResume size = {this.state.size} {...this.props}/>
                </div>

                <div className="Product-Message">
                    Do you wanna got some?
                </div>
                <div className="Product-Search-Input">
                    <Segment inverted size='mini'>

                        <Input inverted icon={<Icon name='search' inverted circular link/>}
                               placeholder='Search...'
                               onChange={this.changeHandler}/>
                    </Segment>
                </div>
                <div>{this.state.change}</div>

                <div className="Product-Container">
                    <Grid centered columns={3}>
                        <Grid.Row centered>

                            {this.state.products.map((product, index) => (
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

export default Products;