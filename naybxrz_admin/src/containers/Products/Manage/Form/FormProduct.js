import React, {PureComponent} from "react";
import {connect} from 'react-redux';
import {
    Button,
    Dimmer,
    Dropdown,
    Form,
    Grid,
    Icon,
    Image,
    Label,
    List,
    Loader,
    Modal,
    Segment
} from "semantic-ui-react";
import * as actions from "../../../../store/actions";
import './FormProduct.css';
import trash from './trash.png';
import Aux from "../../../../adhoc/Aux/Aux";

class FormProduct extends PureComponent {

    state = {
        id: this.props.productByIdData !== undefined ? this.props.productByIdData.id : '',
        ref: this.props.productByIdData !== undefined ? this.props.productByIdData.ref : '',
        name: this.props.productByIdData !== undefined ? this.props.productByIdData.name : '',
        sex: this.props.productByIdData !== undefined ? this.props.productByIdData.sex : '',
        brand: this.props.productByIdData !== undefined ? this.props.productByIdData.brand : '',
        logo: this.props.productByIdData !== undefined ? this.props.productByIdData.logo : '',
        category: this.props.productByIdData !== undefined ? this.props.productByIdData.category : '',
        price: this.props.productByIdData !== undefined ? this.props.productByIdData.price : '',
        size: this.props.productByIdData !== undefined ? this.props.productByIdData.size : [],
        colors: this.props.productByIdData !== undefined ? this.props.productByIdData.colors : [],
        images: this.props.productByIdData !== undefined ? this.props.productByIdData.images : [],
        availability: this.props.productByIdData !== undefined ? this.props.productByIdData.availability : [],
        available: this.props.productByIdData !== undefined ? this.props.productByIdData.available : '',
        quantity: this.props.productByIdData !== undefined ? this.props.productByIdData.quantity : '',
        dimension: this.props.productByIdData !== undefined ? this.props.productByIdData.dimension : null,
        originalPrice: this.props.productByIdData !== undefined ? this.props.productByIdData.originalPrice : '',
        promotion: this.props.productByIdData !== undefined ? this.props.productByIdData.promotion : '',
        store: this.props.productByIdData !== undefined ? this.props.productByIdData.store : '',
        dateTime: this.props.productByIdData !== undefined ? this.props.productByIdData.dateTime : '',
        showDelete: true,
        showAddImage: false,
        newImage: '',
        showAddColor: false,
        newColor: '',
        newQte: 0,
        showAddAvailability: false
    }

    refreshState = () => {
        this.setState({
            ref: this.props.productByIdData !== undefined ? this.props.productByIdData.ref : '',
            name: this.props.productByIdData !== undefined ? this.props.productByIdData.name : '',
            sex: this.props.productByIdData !== undefined ? this.props.productByIdData.sex : '',
            brand: this.props.productByIdData !== undefined ? this.props.productByIdData.brand : '',
            logo: this.props.productByIdData !== undefined ? this.props.productByIdData.logo : '',
            category: this.props.productByIdData !== undefined ? this.props.productByIdData.category : '',
            price: this.props.productByIdData !== undefined ? this.props.productByIdData.price : '',
            size: this.props.productByIdData !== undefined ? this.props.productByIdData.size : [],
            colors: this.props.productByIdData !== undefined ? this.props.productByIdData.colors : [],
            images: this.props.productByIdData !== undefined ? this.props.productByIdData.images : [],
            availability: this.props.productByIdData !== undefined ? this.props.productByIdData.availability : [],
            available: this.props.productByIdData !== undefined ? this.props.productByIdData.available : '',
            quantity: this.props.productByIdData !== undefined ? this.props.productByIdData.quantity : '',
            dimension: this.props.productByIdData !== undefined ? this.props.productByIdData.dimension : null,
            originalPrice: this.props.productByIdData !== undefined ? this.props.productByIdData.originalPrice : '',
            promotion: this.props.productByIdData !== undefined ? this.props.productByIdData.promotion : '',
            store: this.props.productByIdData !== undefined ? this.props.productByIdData.store : '',
            dateTime: this.props.productByIdData !== undefined ? this.props.productByIdData.dateTime : '',
        });
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    handleChangeSex = (e, {value}) => this.setState({value, sex: value});

    handleChangeCategory = (e, {value}) => this.setState({value, category: value});

    handleChangeStore = (e, {value}) => this.setState({value, store: value});

    handleChangeAvailable = (e, {value}) => this.setState({value, available: value});

    handleChangeSize = (e, {value}) => this.setState({size: value});

    handleChangeColor = (e, {value}) => this.setState({colors: value});

    handleChangeNewQte = (e, {value}) => this.setState({newQte: value});

    closeModal = () => this.props.closeProductByIdPopup(this.props.history);

    showDeleteButton = () => this.setState({showDelete: !this.state.showDelete});

    showAddImage = () => this.setState({showAddImage: !this.state.showAddImage});

    showAddColor = () => this.setState({showAddColor: !this.state.showAddColor});

    showAddAvailability = () => this.setState({showAddAvailability: !this.state.showAddAvailability});


    addNewImage = () => {
        let images = [...this.state.images];
        images.push(this.state.newImage);
        this.setState({
            images: images,
            showAddImage: false
        });
    };

    addNewColor = () => {
        let colors = [...this.state.colors];
        colors.push(this.state.newColor);
        this.setState({
            colors: colors,
            showAddColor: false
        });
        this.addAvailabilityByNewColor();
    }

    removeImage = (id) => {
        let images = [...this.state.images];
        images.splice(id, 1);
        this.setState({images: images});
    }

    checkAvailableIcon() {
        if (this.state.available) {
            return 'green'
        } else {
            return 'red'
        }
    }

    incrementSize(key, size) {
        let availabilityToSave = {...this.state.availability};
        let sizeQt = availabilityToSave[key];
        for (let sq of sizeQt) {
            if (sq.size === size) {
                sq.qte++;
            }
        }
        availabilityToSave[key] = sizeQt;
        this.setState({availability: availabilityToSave});
    }

    decrementSize(key, size) {
        let availabilityToSave = {...this.state.availability};
        let sizeQt = availabilityToSave[key];
        for (let sq of sizeQt) {
            if (sq.size === size) {
                sq.qte--;
            }
        }
        availabilityToSave[key] = sizeQt;
        this.setState({availability: availabilityToSave});
    }

    // for New Product
    addAvailability(col, size) {
        let availability = {...this.state.availability};
        if (availability[col] === undefined) {
            availability[col] = [{'size': size, 'qte': this.state.newQte}];
            this.setState({availability: availability});
        } else {
            let oldAv = {...this.state.availability};
            let oldSizeQte = [...oldAv[col]];
            let existingSize = false;
            for (var existSize of oldSizeQte) {
                if (existSize.size === size) {
                    existSize.qte = this.state.newQte;
                    existingSize = true;
                }
            }
            if (!existingSize) {
                let newSizeQte = {'size': size, 'qte': this.state.newQte};
                oldSizeQte.push(newSizeQte);
            }
            oldAv[col] = oldSizeQte;
            this.setState({availability: oldAv});
        }

    }

    // for Edit Product
    addAvailabilityByNewColor(){
        if (this.props.editMode){
            let currentAvailability = {...this.state.availability};
            let oldSize = [];
            for (var sz of this.state.size){
                oldSize.push({'size': sz, 'qte': 0});
            }
            currentAvailability[this.state.newColor] = oldSize
            this.setState({availability: currentAvailability});
        }
    }

    saveProduct = () => {
        this.props.saveProduct(this.createProduct(), this.props.history);
    }

    createProduct() {
        return {
            id: this.state.id,
            ref: this.state.ref,
            name: this.state.name,
            sex: this.state.sex,
            brand: this.state.brand,
            logo: this.state.logo,
            category: this.state.category,
            price: this.state.price,
            size: this.state.size,
            colors: this.state.colors,
            images: this.state.images,
            availability: this.state.availability,
            available: this.state.available,
            quantity: this.state.quantity,
            dimension: this.state.dimension,
            originalPrice: this.state.originalPrice,
            promotion: this.state.promotion,
            store: this.state.store,
            dateTime: this.state.dateTime
        }
    }


    render() {

        let optionsStore = [
            {key: 1, text: 'CA', value: 'CA'},
            {key: 2, text: 'FR', value: 'FR'}
        ];

        let optionsSize = [
            {key: 1, text: 'Small', value: 'S'},
            {key: 2, text: 'Medium', value: 'M'},
            {key: 3, text: 'Large', value: 'L'},
            {key: 4, text: 'XLarge', value: 'XL'},
        ];

        let colors = [];
        if (this.state.colors) {
            this.state.colors.forEach(c => {
                colors.push({
                    key: c, text: c, value: c
                })
            });
        }

        let sexOptions = [
            {key: 0, text: '----', value: ''},
            {key: 1, text: 'Men', value: 'M'},
            {key: 2, text: 'Women', value: 'W'},
        ]

        let availableOptions = [
            {key: 1, text: 'Available', value: true},
            {key: 2, text: 'Sold out', value: false},
        ]

        let categories = [
            {key: 1, text: 'TShirt', value: 'Tshirt'},
            {key: 2, text: 'Sweat', value: 'Sweat'},
            {key: 3, text: 'Jacket', value: 'Jacket'},
            {key: 4, text: 'Hat', value: 'Hat'},
            {key: 5, text: 'Accessories', value: 'Accessories'}
        ]

        let formAddImage = undefined;

        if (this.state.showAddImage) {
            formAddImage = <Form>
                <Form.Group inline>
                    <Form.Input width='10' fluid
                                placeholder='New image URL'
                                name='newImage'
                                icon='linkify'
                                value={this.state.newImage}
                                onChange={this.handleChange}
                    />
                    <Icon name="add"
                          color='green'
                          size='big'
                          className="product-delete-button"
                          onClick={this.addNewImage}/>
                </Form.Group>
            </Form>

        }


        let formAddColor = undefined;

        if (this.state.showAddColor) {
            formAddColor =
                <Form.Group inline>
                    <Form.Input width='10' fluid
                                placeholder='New Color'
                                name='newColor'
                                value={this.state.newColor}
                                onChange={this.handleChange}
                    />
                    <Icon name="add"
                          color='green'
                          size='big'
                          className="product-delete-button"
                          onClick={this.addNewColor}/>
                </Form.Group>
        }

        let availabilityBlock = undefined;
        if (this.props.editMode) {
            availabilityBlock =
                <Form.Group grouped>
                    {Object.keys(this.state.availability).map((key, idx) => (
                        <Form.Group key={idx} inline>
                            <Label size='small'
                                   basic
                                   color='green'>Color:</Label>
                            <span style={{margin: '0 10% 0 1%', fontWeight: 'bold'}}>{key}</span>
                            {this.state.availability[key].map((av, indV) => (
                                <List key={indV} horizontal relaxed>
                                    <List.Item>
                                        <Label size='small'
                                               basic
                                               color='green'>Size:</Label>
                                    </List.Item>
                                    <List.Item>
                                        <span style={{fontWeight: 'bold'}}>{av.size}</span>
                                    </List.Item>
                                    <List.Item>
                                        <span>{av.qte}</span>
                                    </List.Item>
                                    <List.Item>
                                        <Button circular icon='add' size='small' color='green'
                                                onClick={() => this.incrementSize(key, av.size)}/>
                                        <Button circular icon='delete' size='small' color='red'
                                                onClick={() => this.decrementSize(key, av.size)}/>
                                    </List.Item>
                                </List>
                            ))}
                        </Form.Group>
                    ))}
                </Form.Group>
        }

        let buttonAddAvailability = undefined;
        if (this.state.availability.length === 0) {
            buttonAddAvailability = <Button
                icon='add'
                content="Add Availability"
                labelPosition='right'
                color='green'
                size='small'
                onClick={this.showAddAvailability}/>
        }

        let addAvailabilty = undefined;
        if (this.state.showAddAvailability) {
            addAvailabilty = <Aux>
                {this.state.colors.map((col, idxC) => (
                    <Form.Group key={idxC} widths='equal'>
                        <Label className="product-availability-align-label"
                               size='small'
                               basic
                               color='green'>Color:</Label>
                        <span className="product-availability-align">{col}</span>
                        <List.Item className="product-availability-align-label">
                            <Label
                                   size='small'
                                   basic
                                   color='green'>Size:</Label>
                        </List.Item>
                        {this.state.size.map((sz, id) => (
                            <Form.Group key={id} widths='equal' inline>
                                <List key={sz} horizontal>
                                    <List.Item>
                                        <span className="product-availability-align">{sz}</span>
                                    </List.Item>
                                    <List.Item>
                                        <Form.Input label="Qte."
                                                    name="newQte"
                                                    value={this.state.newQte}
                                                    onChange={this.handleChangeNewQte}/>
                                    </List.Item>
                                    <List.Item>
                                        <Button circular
                                                icon='add'
                                                size='tiny'
                                                color='green'
                                                onClick={() => this.addAvailability(col, sz)}/>
                                    </List.Item>
                                </List>

                            </Form.Group>
                        ))}
                    </Form.Group>
                ))}</Aux>
        }

        let resumeAvailabilityForAddNewProduct = undefined;
        if (!this.props.editMode && Object.keys(this.state.availability).length > 0) {
            resumeAvailabilityForAddNewProduct =
                <Segment.Group>
                    <Segment>
                        <Icon name='boxes'/>
                        <span style={{fontWeight: 'bold'}}>Availability Summary</span>
                    </Segment>
                    <Segment.Group>
                        <Segment>
                    <Grid>
                        {Object.keys(this.state.availability).map((keyV, idxV) => (
                            <Grid.Row key={idxV}>
                                <Grid.Column width={2}>
                                    <Label color='red' basic content='Color:'/>
                                </Grid.Column>
                                <Grid.Column width={2}>
                                    <span style={{fontWeight: 'bold'}}>{keyV}</span>
                                </Grid.Column>
                                <Grid.Column width={2}>
                                    <Label color='red' basic content='Size:'/>
                                </Grid.Column>
                                {this.state.availability[keyV].map((avl, indV) => (
                                    <Grid.Column key={indV} width={4}>
                                        <Label color='green' circular inverted="true" content={avl.size}/>
                                        <Label color='green' circular inverted="true" content={avl.qte}/>
                                    </Grid.Column>
                                ))}
                            </Grid.Row>
                        ))}
                    </Grid>
                        </Segment>
                    </Segment.Group>
                </Segment.Group>
        }


        return (
            <Modal open={this.props.productByIdPopup}
                   onClose={this.closeModal}
                   size='large' onOpen={this.refreshState} onMount={this.refreshState}>
                <Dimmer active={this.props.saveProductLoading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <Modal.Content>
                    <Segment.Group>
                        <Segment>
                            <Icon name='archive'/>
                            <span style={{fontWeight: 'bold'}}>Product Info</span>
                        </Segment>
                        <Segment.Group>
                            <Segment>
                                <Form>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='Product ID:'
                                                    name='id'
                                                    value={this.state.id}
                                                    disabled={this.props.editMode}
                                                    inverted
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Date Time:'
                                                    name='dateTime'
                                                    value={this.state.dateTime}
                                                    disabled/>
                                        <Image avatar size='small' src={this.state.logo}/>
                                    </Form.Group>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='Ref:'
                                                    name='ref'
                                                    value={this.state.ref}
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Name:'
                                                    name='name'
                                                    value={this.state.name}
                                                    onChange={this.handleChange}/>
                                        <Form.Dropdown options={availableOptions}
                                                       style={{
                                                           fontWeight: 'bold',
                                                           color: this.checkAvailableIcon()
                                                       }}
                                                       label="Status:"
                                                       name='available'
                                                       value={this.state.available}
                                                       onChange={this.handleChangeAvailable}/>
                                    </Form.Group>
                                </Form>
                            </Segment>
                        </Segment.Group>

                        <Segment>
                            <Icon name='barcode'/>
                            <span style={{fontWeight: 'bold'}}>Product Details</span>
                        </Segment>
                        <Segment.Group>
                            <Segment>
                                <Form>
                                    <Form.Group widths='equal' inline>
                                        <Form.Dropdown onChange={this.handleChangeSex}
                                                       placeholder='Sex:'
                                                       label='Sex:'
                                                       options={sexOptions}
                                                       selection
                                                       value={this.state.sex}/>
                                        <Form.Dropdown onChange={this.handleChangeCategory}
                                                       placeholder='Category:'
                                                       label='Category:'
                                                       options={categories}
                                                       selection
                                                       value={this.state.category}/>
                                        <Form.Dropdown onChange={this.handleChangeStore}
                                                       placeholder='Store:'
                                                       label='Store:'
                                                       options={optionsStore}
                                                       selection
                                                       value={this.state.store}/>
                                    </Form.Group>
                                    <Form.Group inline widths='equal'>
                                        <Form.Input
                                            icon='dollar'
                                            label='Price:'
                                            name='price'
                                            value={this.state.price}
                                            onChange={this.handleChange}/>
                                        <Form.Input
                                            icon='dollar'
                                            label='Original Price:'
                                            name='originalPrice'
                                            value={this.state.originalPrice}
                                            onChange={this.handleChange}/>
                                        <Form.Input
                                            icon='tag'
                                            label='Promotion:'
                                            name='promotion'
                                            value={this.state.promotion}
                                            onChange={this.handleChange}/>

                                    </Form.Group>

                                    <Form.Group widths='equal' inline>
                                        <Form.Input fluid
                                                    label='Brand:'
                                                    name='brand'
                                                    value={this.state.brand}
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='URL logo:'
                                                    name='logo'
                                                    icon='linkify'
                                                    value={this.state.logo}
                                                    onChange={this.handleChange}/>
                                    </Form.Group>
                                    <Icon name='add'
                                          size='large'
                                          color='green'
                                          className="product-delete-button"
                                          onClick={this.showAddImage}/>
                                    <Icon name='delete'
                                          color='red'
                                          size='large'
                                          className="product-delete-button"
                                          onClick={this.showDeleteButton}/>
                                    {formAddImage}
                                    {this.state.images.map((image, idx) => (
                                        <Form.Group key={idx} inline>
                                            <Image size='small'
                                                   src={image}/>
                                            <Image size='mini'
                                                   src={trash}
                                                   className="product-delete-button"
                                                   disabled={this.state.showDelete}
                                                   onClick={() => this.removeImage(idx)}/>
                                        </Form.Group>
                                    ))}

                                    <Segment>
                                        <Icon name='shopping bag'/>
                                        <span style={{fontWeight: 'bold'}}>Availability</span>

                                        <Form.Group>
                                            <Dropdown
                                                placeholder='Size'
                                                fluid
                                                multiple
                                                selection
                                                options={optionsSize}
                                                value={this.state.size}
                                                onChange={this.handleChangeSize}/>
                                            <Dropdown
                                                placeholder='Colors'
                                                fluid
                                                multiple
                                                selection
                                                options={colors}
                                                value={this.state.colors}
                                                onChange={this.handleChangeColor}/>
                                            <Icon className="product-delete-button"
                                                  name='add'
                                                  color='green'
                                                  size='big'
                                                  onClick={this.showAddColor}/>
                                        </Form.Group>
                                        {formAddColor}
                                        {availabilityBlock}
                                        {resumeAvailabilityForAddNewProduct}
                                        {buttonAddAvailability}
                                        {addAvailabilty}
                                    </Segment>
                                </Form>
                            </Segment>
                        </Segment.Group>
                    </Segment.Group>

                    <Modal.Actions>
                        <Button className="product-save-button"
                                color='black'
                                floated='right'
                                onClick={this.saveProduct}>
                            <span className="product-save-button-text">SAVE</span>
                            <Icon name='right chevron' color='yellow'/>
                        </Button>
                    </Modal.Actions>
                </Modal.Content>
            </Modal>
        );
    }
}

const mapStateToProps = state => {
    return {
        productByIdPopup: state.products.productByIdPopup,
        saveProductError: state.products.saveProductError,
        saveProductLoading: state.products.saveProductLoading
    }
}

const dispatchToProps = dispatch => {
    return {
        closeProductByIdPopup: (history) => dispatch(actions.closeProductModalAndRedirectBack(history)),
        saveProduct: (product, history) => dispatch(actions.saveProduct(product, history))
    }
}

export default connect(mapStateToProps, dispatchToProps)(FormProduct);