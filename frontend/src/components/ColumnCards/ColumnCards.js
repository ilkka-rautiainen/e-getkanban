import React, { PropTypes } from 'react';
import { connect } from 'react-redux';
import {TransitionMotion, spring, presets} from 'react-motion';
import { setActiveCard } from '../../actions';
import Card from '../Card/Card';

class ColumnCards extends React.Component {

  constructor({ phase }) {
    super();
    this.phase = phase;
  }

  get getDefaultStyles() {
    const styles = this.props.columnCardIds.map((cardId) => {
      return {
        key: cardId,
        style: { opacity: 0.0 }
      }
    });
    return styles;
  }

  get getStyles() {
    const styles = this.props.columnCardIds.map((cardId) => {
      return {
        key: cardId,
        style: {
          opacity: spring(1, {stiffness: 220, damping: 42})
        }
      }
    });
    return styles;
  }

  willEnter() {
    return { opacity: 0.0 }
  }

  willLeave() {
    return {
      opacity: spring(0, presets.gentle)
    };
  }

  handleCardClick(event, cardId, phaseId) {
    if (this.phase.isWorkPhase) {
      const cardPageX = event.nativeEvent.pageX;
      const cardPageY = event.nativeEvent.pageY;
      this.props.onSetActiveCard(cardId, phaseId, cardPageX, cardPageY);
    }
  }

  render() {
    if (this.props.columnCardIds) {
      return (
        <TransitionMotion
          defaultStyles={this.getDefaultStyles}
          styles={this.getStyles}
          willEnter={this.willEnter}
          willLeave={this.willLeave}
        >
          {styles =>
            <div className="column-cards">
              {styles.map((card) => {
                return (
                  <Card key={card.key} id={card.key} style={card.style} onClick={e => this.handleCardClick(e, card.key, this.phase.id)} />
                )
              })}
            </div>
          }
        </TransitionMotion>
      )
    } else {
      return null;
    }
  }

}

ColumnCards.propTypes = {
  columnCardIds: PropTypes.arrayOf(PropTypes.string.isRequired),
  phase: PropTypes.object.isRequired
};

const mapDispatchToProps = (dispatch) => {
  return {
    onSetActiveCard: (cardId, phaseId, cardPageX, cardPageY) => {
      dispatch(setActiveCard(cardId, phaseId, cardPageX, cardPageY))
    }
  }
};

export default connect(
  null,
  mapDispatchToProps
)(ColumnCards);
