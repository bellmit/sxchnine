import React, {Component} from "react";
import {Form, Grid, Icon, Input, Segment} from 'semantic-ui-react';
import { Collapse } from "@chakra-ui/core";
import Product from './Product';
import BannerMen from '../../components/Banner/Banner';
import Contact from '../Contact/Contact';
import ShopResume from "../ShopResume/ShopResume";
import './Products.css';
import addIcon from './add-icon.jpg';
import searchIcon from './searc-icon-tiny.png';

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
        show: false,
        size: 0
    }

    componentDidMount() {
        console.log("Products.js " + this.props);
    }

    changeHandler = (event) => {
        this.setState({
            change: event.target.value
        })
    }

    selectProductHandler = (id) => {
        this.props.history.push('/products/' + id);
    }

    toggleAdvancedSearch = () => {
        this.setState((state) => ({
            show: !state.show
        }))
    }

    render() {

        const genre = [
            {key: '1', text: 'Hoodie', value: 'hoodie'},
            {key: '2', text: 'T-Shirt', value: 'tshirt'},
            {key: '3', text: 'Sweatshirt', value: 'sweatshirt'},
            {key: '4', text: 'Jacket', value: 'jacket'},
        ]

        const options = [
            {key: 'm', text: 'Male', value: 'male'},
            {key: 'f', text: 'Female', value: 'female'},
            {key: 'o', text: 'Other', value: 'other'},
        ]

        const size = [
            {key: '1', text: 'Small', value: 'small'},
            {key: '2', text: 'Medium', value: 'medium'},
            {key: '3', text: 'Large', value: 'large'},
            {key: '4', text: 'XL', value: 'xl'},
        ]

        return (
            <div>
                <div>
                    <header>
                        <BannerMen {...this.props}/>
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
                               onChange={this.changeHandler} style={{marginBottom: '10px'}}/>

                        <img alt="" src={addIcon} className="Add-Icon" onClick={this.toggleAdvancedSearch}/>
                        <Collapse isOpen={this.state.show}>
                            <Form size='tiny'>
                                <Form.Group inline widths='1' unstackable>
                                    <Form.Select
                                        width={6}
                                        options={options}
                                        placeholder='SEXE'/>
                                    <Form.Select
                                        width={6}
                                        options={genre}
                                        placeholder='Genre'/>
                                    <Form.Select
                                        width={6}
                                        options={size}
                                        placeholder='Size'/>
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