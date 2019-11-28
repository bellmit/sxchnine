import React, {Component} from 'react';
import {Form, Button, Icon, Grid, Label} from 'semantic-ui-react';
import {Badge, CSSReset, ThemeProvider} from "@chakra-ui/core";
import './ProductDetails.css';

class ProductDetails extends Component {
    state = {
        availability: true
    };

    handleChange = (e, {value}) => this.setState({value})

    render() {
        const {value} = this.state;
        let badge = null;

        const options = [
            {key: 'm', text: 'Male', value: 'male'},
            {key: 'f', text: 'Female', value: 'female'},
            {key: 'o', text: 'Other', value: 'other'},
        ]

        if (this.state.availability) {
            badge = (
                <div>
                    <ThemeProvider>
                        <CSSReset/>
                        <Badge rounded="full" px="2" variantColor="teal">
                            Available
                        </Badge>
                    </ThemeProvider>
                </div>
            )
        } else {
            badge = (
                <div>
                    <ThemeProvider>
                        <CSSReset/>
                        <Badge rounded="full" px="2" variantColor="red">
                            Soldout
                        </Badge>
                    </ThemeProvider>
                </div>
            )
        }

        return (
            <div>
                <div>
                    <p className="Product-Name-Div">Classic Hoodie - 90's</p>
                </div>
                <div className="Product-Form-Div">
                    <Form unstackable widths='equal' size='large'>

                        <Form.Group inline widths='equal'>
                            <span className="Product-Props-Div">COLOR:&nbsp;&nbsp;&nbsp;</span>
                            <Form.Select

                                fluid
                                options={options}
                                placeholder='Color'/>
                        </Form.Group>
                        <Form.Group inline widths='equal'>
                            <span className="Product-Props-Div">SIZE:&nbsp;&nbsp;&nbsp;</span>
                            <Form.Radio
                                label='Small'
                                value='sm'
                                checked={value === 'sm'}
                                onChange={this.handleChange}
                            />
                            <Form.Radio
                                label='Medium'
                                value='md'
                                checked={value === 'md'}
                                onChange={this.handleChange}
                            />
                            <Form.Radio
                                label='Large'
                                value='lg'
                                checked={value === 'lg'}
                                onChange={this.handleChange}
                            />
                        </Form.Group>
                        <Form.Group inline widths='equal'>
                            <span className="Product-Props-Div">PRICE:&nbsp;&nbsp;&nbsp;</span>
                            <Label tag color='red'>
                                $90
                            </Label>
                        </Form.Group>
                        <Form.Group>
                            <a href="/delivery" className="Product-Delivery-Div">&nbsp;&nbsp;&nbsp;Delivery & return info</a>
                        </Form.Group>
                        <Form.Group>
                            {badge}
                        </Form.Group>
                        <Button animated='vertical' inverted color='yellow' floated='right'>
                            <Button.Content hidden>Got it!</Button.Content>
                            <Button.Content visible>
                                <Icon name='shop' inverted color='yellow'/>
                            </Button.Content>
                        </Button>
                    </Form>
                </div>

                <div className="Grid-Container-Div">
                    <Grid columns={1} verticalAlign='bottom'>
                        <Grid.Row stretched>
                            <Grid.Column>
                                <p className="Product-Grid-Div">PRODUCT DETAILS:</p>
                                <p className="Product-Grid-Div">100% COTTON</p>
                            </Grid.Column>
                        </Grid.Row>

                        <Grid.Row stretched>
                            <Grid.Column>
                                <p className="Product-Grid-Div">MODEL DETAILS:</p>
                                <p className="Product-Grid-Div">HEIGHT 170cm/79</p>
                                <p className="Product-Grid-Div">SIZE: S</p>

                            </Grid.Column>
                        </Grid.Row>
                    </Grid>
                </div>

            </div>
        );
    }
}

export default ProductDetails;