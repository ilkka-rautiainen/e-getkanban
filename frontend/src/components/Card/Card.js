import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import Paper from 'material-ui/Paper';

const Card = ({ card }) => {
  return <div className="Card">
    <Paper>This is a card</Paper>
  </div>
};

Card.propTypes = {
  card: PropTypes.object.isRequired
};

const mapStateToProps = (state, ownProps) => {
  return {
    card: state.cards[ownProps.id]
  }
};

export default connect(
  mapStateToProps
)(Card);
