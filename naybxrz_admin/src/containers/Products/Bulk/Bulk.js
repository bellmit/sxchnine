import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Dimmer, Grid, Label, Loader, TextArea} from "semantic-ui-react";
import * as actions from "../../../store/actions/index";
import './Bulk.css';

class Bulk extends Component {

    state = {
        products: []
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        console.log(this.props.bulkProductsError);
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    saveProducts = () => {
        this.props.bulkProducts(this.state.products);
    }

    render() {

        let errors = undefined;
        if (this.props.bulkProductsError){
            errors = <Label color='red'>{this.props.bulkProductsError.status} - {this.props.bulkProductsError.message}</Label>
        }

        let success = undefined;
        if (this.props.bulkProductsSuccess){
            success = <Label color='green'>Bulk was successfully completed</Label>
        }

        return (
            <div className="div-bulk-products">
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading...'/>
                </Dimmer>
                {errors}
                {success}
                <Grid centered>
                    <Grid.Row>
                        <Grid.Column width={15}>
                        <TextArea rows={40} style={{width: '300%'}}
                                  name='products'
                                  value={this.state.products}
                                  placeholder='Enter your products on array json format'
                                  onChange={this.handleChange}/>
                        </Grid.Column>
                    </Grid.Row>
                    <Grid.Row>
                        <Button className="product-bulk-button"
                                color='black'
                                floated='right'
                                icon='cogs'
                                content="Process"
                                onClick={this.saveProducts}>
                        </Button>
                    </Grid.Row>
                </Grid>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.products.bulkProductsLoading,
        bulkProductsSuccess: state.products.bulkProductsSuccess,
        bulkProductsError: state.products.bulkProductsError
    }
}

const dispatchToProps = dispatch => {
    return {
        bulkProducts: (products) => dispatch(actions.bulkProducts(products))
    }
}


export default connect(mapStateToProps, dispatchToProps)(Bulk);